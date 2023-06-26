package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.integration

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever
import org.springframework.boot.test.mock.mockito.MockBean
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service.ChartService

class ChartControllerTest : IntegrationTestBase() {

  @MockBean
  private lateinit var chartService: ChartService

  @BeforeEach
  fun beforeEach() {
    whenever(
      chartService.getChart(
        any(),
        any(),
        anyOrNull(),
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
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("agencyId").isEqualTo("ACI")
      .jsonPath("chartName").isEqualTo("1a")
      .jsonPath("chartEntries.length()").isEqualTo(2)
  }

  @Test
  fun `Get chart data by agencyId and characteristic`() {
    // Expected structure: {"agencyId":"ACI","chartName":"1a","chartEntries":[ ... mocked ] }

    webTestClient.get()
      .uri("/api/data-insights/chart/ACI/1a?characteristic?ethnic_group")
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("agencyId").isEqualTo("ACI")
      .jsonPath("chartName").isEqualTo("1a")
      .jsonPath("chartEntries.length()").isEqualTo(2)
  }
}
