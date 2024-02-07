package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.Chart
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.ChartMetadataDto
import java.time.ZoneId

@Service
class ChartService(private val s3Facade: S3Facade) {

  @Cacheable("charts", key = "#agencyId + #chart.name")
  fun getChart(agencyId: String, chart: Chart): List<Map<String, Any>> {
    val fileAsString = this.s3Facade.getFile(chart.fileName)
    val items: Map<String, List<Map<String, Any>>> =
      Gson().fromJson(fileAsString, object : TypeToken<Map<String, List<Map<String, Any>>>>() {}.type)

    return items[agencyId].orEmpty()
  }

  @Cacheable("chart-meta", key = "#chart.name")
  fun getS3ObjectMetaData(chart: Chart): ChartMetadataDto {
    val s3Metadata = this.s3Facade.getS3ObjectMetadata(chart.fileName)
    return ChartMetadataDto(
      chartName = chart.fileName,
      lastModifiedDate = s3Metadata.lastModified.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
    )
  }
}
