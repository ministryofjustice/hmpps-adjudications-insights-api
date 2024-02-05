package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.Chart
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.ChartMetadataDto
import java.time.ZoneId

@Service
class ChartService(private val s3Facade: S3Facade) {

  private val chartMap = mutableMapOf<Chart, String>()

  fun getChart(agencyId: String, chart: Chart): List<Map<String, Any>> {
    val fileAsString = chartMap[chart] ?: this.s3Facade.getFile(chart.fileName)
    val items: Map<String, List<Map<String, Any>>> =
      Gson().fromJson(fileAsString, object : TypeToken<Map<String, List<Map<String, Any>>>>() {}.type)

    return items[agencyId].orEmpty()
  }

  fun getS3ObjectMetaData(chart: Chart): ChartMetadataDto {
    val s3Metadata = this.s3Facade.getS3ObjectMetadata(chart.fileName)
    return ChartMetadataDto(
      chartName = chart.fileName,
      lastModifiedDate = s3Metadata.lastModified.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
    )
  }

  @Scheduled(cron = "@hourly")
  private fun checkForLatest() {
    log.info("updating charts")
    Chart.values().forEach {
      chartMap[it] = this.s3Facade.getFile(it.fileName)
    }
  }

  @PostConstruct
  private fun initCharts() {
    checkForLatest()
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}
