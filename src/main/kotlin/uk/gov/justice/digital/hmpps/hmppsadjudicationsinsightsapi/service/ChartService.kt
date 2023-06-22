package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.Chart
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.ChartDataDto

@Service
class ChartService(private val s3Facade: S3Facade) {

  fun getChart(agencyId: String, chart: Chart): List<ChartDataDto> {
    val fileAsString = this.s3Facade.getFile(chart.fileName)
    val items: Map<String, List<ChartDataDto>> =
      Gson().fromJson(fileAsString, object : TypeToken<Map<String, List<ChartDataDto>>>() {}.type)

    return items[agencyId].orEmpty()
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}
