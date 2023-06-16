package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service


import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChartServiceTest {

  @Test
  fun x () {
    val chartService = ChartService()

    assertThat(chartService.getChart("SKI").incident_prison).isEqualTo("SKI")
  }

  @Test
  fun y ()   {
    val chartService = ChartService()

    val x = runBlocking {
      chartService.getChart2("SKI")?.incident_prison
    }

    assertThat(x).isEqualTo("SKI")
  }

}