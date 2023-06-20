package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.integration

import org.junit.jupiter.api.Test

class ChartControllerTest: IntegrationTestBase() {

  @Test
  fun `Get chart data by agencyId`() {
    //    {"agencyId":"ACI","chartName":"1a","chartEntries":[
    //    {"month":7,"year_curr":2022,"year_prev":2021,"count_curr":45,"count_prev":93},
    //    ... 12 times in total
    //    ]}

    webTestClient.get()
      .uri("/api/data-insights/chart/ACI/1a")
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("agencyId").isEqualTo("ACI")
      .jsonPath("chartName").isEqualTo("1a")
      .jsonPath("chartEntries.length()").isEqualTo(12)
  }

}