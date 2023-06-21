package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ChartServiceTest {

  private var s3Facade: S3Facade = mock()

  @Test
  fun getFile() {
    val fileContent = this::class.java.classLoader.getResource("test-data/" + "1a_test.json").readText()

    whenever(s3Facade.getFile(any())).thenReturn(fileContent)
    val chartService = ChartService(s3Facade)

    val chart = chartService.getChart("ACI", "1a")
    assertThat(chart.size).isEqualTo(12)
  }
}
