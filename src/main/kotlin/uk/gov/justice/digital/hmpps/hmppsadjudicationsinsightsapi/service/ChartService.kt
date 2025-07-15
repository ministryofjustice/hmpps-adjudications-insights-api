package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.Chart
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.ChartMetadataDto
import java.time.ZoneId

@Service
class ChartService(private val s3Facade: S3Facade) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(ChartService::class.java)
  }

  @Cacheable("charts", key = "#agencyId + #chart.name")
  fun getChart(agencyId: String, chart: Chart): List<Map<String, Any>> {
    try {
      val fileAsString = this.s3Facade.getFile(chart.fileName)
      val items: Map<String, List<Map<String, Any>>> = try {
        Gson().fromJson(fileAsString, object : TypeToken<Map<String, List<Map<String, Any>>>>() {}.type)
      } catch (e: JsonSyntaxException) {
        log.error("Error parsing JSON for chart ${chart.fileName}: ${e.message}")
        mapOf() // Return empty map on parsing error
      }

      return items[agencyId].orEmpty()
    } catch (e: Exception) {
      log.error("Unexpected error getting chart data for ${chart.fileName}: ${e.message}")
      return listOf() // Return empty list on any error
    }
  }

  @Cacheable("chart-meta", key = "#chart.name")
  fun getS3ObjectMetaData(chart: Chart): ChartMetadataDto {
    try {
      val s3Metadata = this.s3Facade.getS3ObjectMetadata(chart.fileName)
      return ChartMetadataDto(
        chartName = chart.fileName,
        lastModifiedDate = s3Metadata.lastModified?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
          ?: java.time.LocalDateTime.now(), // Fallback to current time if lastModified is null
      )
    } catch (e: Exception) {
      log.error("Error getting metadata for chart ${chart.fileName}: ${e.message}")
      // Return a default ChartMetadataDto with current time
      return ChartMetadataDto(
        chartName = chart.fileName,
        lastModifiedDate = java.time.LocalDateTime.now(),
      )
    }
  }
}
