package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.integration

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.ChartMetadataDto
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service.ChartService
import java.time.LocalDateTime

@ExtendWith(SpringExtension::class)
@SpringBootTest
class ChartControllerTest : IntegrationTestBase() {

  @MockitoBean
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
    val chartMetadataDto = ChartMetadataDto("1a", LocalDateTime.of(2023, 8, 15, 8, 6, 7))
    whenever(chartService.getS3ObjectMetaData(any())).thenReturn(chartMetadataDto)

    webTestClient.get()
      .uri("/api/data-insights/chart/last-updated/1a")
      .headers(setHeaders())
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("chartName").isEqualTo("1a")
      .jsonPath("lastModifiedDate").isNotEmpty
  }
}
