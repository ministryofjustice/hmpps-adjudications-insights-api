package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.amazonaws.services.s3.model.ObjectMetadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.Chart

class ChartServiceTest {

  private val s3Facade: S3Facade = mock()
  private val chartService = ChartService(s3Facade)

  @EnumSource(Chart::class)
  @ParameterizedTest
  fun `get chart`(chart: Chart) {
    val filePath = "test-data/${chart.fileName}"
    val fileContent = this::class.java.classLoader.getResource(filePath)?.readText()

    whenever(s3Facade.getFile(chart.fileName)).thenReturn(fileContent)

    val chartDetails = chartService.getChart(agencyId = "ACI", chart = chart)
    assertThat(chartDetails).isNotEmpty
  }

  @EnumSource(Chart::class)
  @ParameterizedTest
  fun `get S3 Bucket metadata of Chart`(chart: Chart) {
    whenever(s3Facade.getS3ObjectMetadata(chart.fileName)).thenReturn(ObjectMetadata())

    val s3ObjectMetadata = chartService.getS3ObjectMetaData(chart = chart)
    assertThat(s3ObjectMetadata).isNotNull
  }
}
