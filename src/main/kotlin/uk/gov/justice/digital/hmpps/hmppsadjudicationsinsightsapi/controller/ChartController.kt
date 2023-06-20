package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.ChartDataResponseDto
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service.ChartService

@RestController
@RequestMapping("/api/data-insights/chart")
class ChartController {

  @Autowired
  private lateinit var chartService: ChartService

  @GetMapping("/{incidentId}/{chartName}")
  fun getChart(
    @PathVariable(name = "incidentId") incidentId: String,
    @PathVariable(name = "chartName") chartName: String,
  ): ChartDataResponseDto {
    val chart = chartService.getChart(incidentId, chartName)
    return ChartDataResponseDto(incidentId, chartName, chart.get())
  }



}