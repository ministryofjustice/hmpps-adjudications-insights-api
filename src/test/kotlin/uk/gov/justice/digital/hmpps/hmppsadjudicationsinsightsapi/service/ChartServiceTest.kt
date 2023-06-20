package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class ChartServiceTest {

  @Mock
  private val s3Service: S3Service = S3Service()

  @BeforeEach
  @Throws(Exception::class)
  fun setUp() {
//    `when`(this.s3Service.initializeChart(anyString())).thenReturn(HashMap())
  }

  @Test
  fun getChart() {

    class ChartServiceMock : ChartService() {
      override fun getS3Service(): S3Service {
        return s3Service
      }
    }
    val chartService = ChartServiceMock()

    val chart = chartService.getChart("ACI", "1a_test.json")
    assertThat(chart).isNotNull
  }

}