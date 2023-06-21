package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.integration

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.boot.test.mock.mockito.MockBean
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.ChartDataDto
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
      ),
    ).thenReturn(
      listOf(
        ChartDataDto(1, 2023, 2022, 50, 40),
        ChartDataDto(12, 2022, 2023, 55, 45),
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
}
