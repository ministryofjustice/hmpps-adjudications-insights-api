package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.Chart

class ChartServiceTest {

  private val s3Facade: S3Facade = mock()
  private val chartService = ChartService(s3Facade)

  @EnumSource(Chart::class)
  @ParameterizedTest
  fun `get chart` (chart: Chart) {
    val fileContent = this::class.java.classLoader.getResource("test-data/${chart.chartName}_test.json")?.readText()

    whenever(s3Facade.getFile(chart.fileName)).thenReturn(fileContent)

    val chart = chartService.getChart(agencyId = "ACI", chart = chart)
    assertThat(chart).isNotEmpty
  }
}
