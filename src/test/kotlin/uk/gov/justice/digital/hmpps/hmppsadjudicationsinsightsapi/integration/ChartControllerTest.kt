package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.integration

import com.amazonaws.services.s3.model.ObjectMetadata
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.boot.test.mock.mockito.MockBean
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service.ChartService
import java.text.SimpleDateFormat

class ChartControllerTest : IntegrationTestBase() {

  @MockBean
  private lateinit var chartService: ChartService

  @BeforeEach
  fun beforeEach() {
    whenever(
      chartService.getChart(
        any(),
        any(),
      ),
    ).thenReturn(
      listOf(
        mapOf(
          "some-key" to 2022,
        ),
        mapOf(
          "some-key" to 2023,
        ),
      ),
    )
  }

  @Test
  fun `Get chart data by agencyId`() {
    // Expected structure: {"agencyId":"ACI","chartName":"1a","chartEntries":[ ... mocked ] }

    webTestClient.get()
      .uri("/api/data-insights/chart/ACI/1a")
      .headers(setHeaders())
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("agencyId").isEqualTo("ACI")
      .jsonPath("chartName").isEqualTo("1a")
      .jsonPath("chartEntries.length()").isEqualTo(2)
  }

  @Test
  fun `Get chart metatadata info by chart name`() {

    val dateStr = "2023-08-17 12:30:45"
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val objectMetadata = ObjectMetadata()
    objectMetadata.lastModified = sdf.parse(dateStr)
    whenever(chartService.getS3ObjectMetaData(any()),).thenReturn(objectMetadata)

    webTestClient.get()
      .uri("/api/data-insights/chart/1a")
      .headers(setHeaders())
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("chartName").isEqualTo("1a")
      .jsonPath("lastModifiedDate").isEqualTo("2023-08-17T12:30:45")
  }
}
