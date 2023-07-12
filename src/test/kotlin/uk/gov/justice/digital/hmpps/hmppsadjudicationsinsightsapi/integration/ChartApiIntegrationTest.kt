package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.integration

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.Chart

class ChartApiIntegrationTest : IntegrationTestBase() {

  @EnumSource(Chart::class)
  @ParameterizedTest
  fun `get chart by name`(chart: Chart) {
    webTestClient.get()
      .uri("/api/data-insights/chart/MDI/${chart.chartName}")
      .headers(setHeaders(roles = listOf("ROLE_VIEW_ADJUDICATIONS_INSIGHTS")))
      .exchange()
      .expectStatus().is2xxSuccessful
  }
}
