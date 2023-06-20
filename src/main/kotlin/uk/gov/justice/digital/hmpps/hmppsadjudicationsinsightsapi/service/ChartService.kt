package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.google.common.base.Optional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.ChartDataDto

@Service
class ChartService {

  @Autowired
  private lateinit var s3Service: S3Service

  fun getChart(incidentId: String, chartName: String): Optional<List<ChartDataDto>> {
    val fullChart = getS3Service().initializeChart("1a_nested_test.json")

    return Optional.fromNullable(fullChart.get(incidentId))
  }

  fun getS3Service() = this.s3Service

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}