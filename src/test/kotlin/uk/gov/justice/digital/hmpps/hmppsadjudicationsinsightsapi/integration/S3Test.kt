package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.integration

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.Chart

class S3Test : IntegrationTestBase() {

  @EnumSource(Chart::class)
  @ParameterizedTest
  fun `test`(chart: Chart) {
    webTestClient.get()
      .uri("/api/data-insights/chart/MDI/${chart.chartName}")
      .exchange()
      .expectStatus().is2xxSuccessful
  }
}
