package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class S3ServiceTest {

  @Test
  fun testChartJson() {
    val service = S3Service()

    val chart = service.initializeChart("1a_test.json")
    assertThat(chart).isNotNull

    val keys = chart.keys
    assertThat(keys.size).isEqualTo(4)

    assertThat(chart.contains("ACI")).isTrue()
    assertThat(chart.contains("BAI")).isTrue()
    assertThat(chart.contains("CDI")).isTrue()
    assertThat(chart.contains("DAI")).isTrue()

    chart.get("ACI")?.let { assertThat(it.size).isEqualTo(12) }
    chart.get("BAI")?.let { assertThat(it.size).isEqualTo(12) }
    chart.get("CDI")?.let { assertThat(it.size).isEqualTo(12) }
    chart.get("DAI")?.let { assertThat(it.size).isEqualTo(12) }
  }

}