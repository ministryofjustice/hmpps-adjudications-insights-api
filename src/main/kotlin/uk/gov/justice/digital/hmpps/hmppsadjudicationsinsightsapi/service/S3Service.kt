package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.ChartDataDto

@Service
class S3Service {

  fun initializeChart(fileName: String): Map<String, List<ChartDataDto>> {
    val fileContent = this::class.java.classLoader.getResource("test-data/" + fileName).readText()

    val items: Map<String, List<ChartDataDto>> =
      Gson().fromJson(fileContent, object : TypeToken<Map<String, List<ChartDataDto>>>() {}.type)

    return items
  }

}