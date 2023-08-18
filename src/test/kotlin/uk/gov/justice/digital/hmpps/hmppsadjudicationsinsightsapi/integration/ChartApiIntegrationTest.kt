package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.integration

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.Chart
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.ChartMetadataDto

class ChartApiIntegrationTest : IntegrationTestBase() {

  @EnumSource(Chart::class)
  @ParameterizedTest
  fun `get chart by name`(chart: Chart) {
    webTestClient.get()
      .uri("/api/data-insights/chart/MDI/${chart.chartName}")
      .headers(setHeaders())
      .exchange()
      .expectStatus().is2xxSuccessful
  }

  @EnumSource(Chart::class)
  @ParameterizedTest
  fun `get chart metadata by name`(chart: Chart) {
    val response = webTestClient.get()
      .uri("/api/data-insights/chart/last-updated/${chart.chartName}")
      .headers(setHeaders())
      .exchange()
      .expectStatus().is2xxSuccessful
      .expectBody(ChartMetadataDto::class.java)
      .returnResult()
      .responseBody!!

    assertNotNull(response.lastModifiedDate)
  }
}
