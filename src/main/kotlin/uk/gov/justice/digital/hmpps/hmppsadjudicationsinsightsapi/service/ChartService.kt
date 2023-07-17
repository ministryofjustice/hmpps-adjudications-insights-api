package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.Chart

@Service
class ChartService(private val s3Facade: S3Facade) {

  fun getChart(agencyId: String, chart: Chart, characteristic: String?): List<Map<String, Any>> {
    val fileAsString = this.s3Facade.getFile(chart.fileName)
    val items: Map<String, List<Map<String, Any>>> =
      Gson().fromJson(fileAsString, object : TypeToken<Map<String, List<Map<String, Any>>>>() {}.type)

    val result = items[agencyId].orEmpty()
    if (characteristic != null) {
      return result.filter { it["characteristic"] == characteristic }
    }
    return result
  }
}
