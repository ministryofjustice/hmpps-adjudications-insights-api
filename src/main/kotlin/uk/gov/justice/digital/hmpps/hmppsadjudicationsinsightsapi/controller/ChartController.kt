package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.Chart
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.ChartDataResponseDto
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service.ChartService

@RestController
@RequestMapping("/api/data-insights/chart")
class ChartController(private val chartService: ChartService) {

  @GetMapping("/{agencyId}/{chartName}")
  fun getChart(
    @PathVariable(name = "agencyId") agencyId: String,
    @PathVariable(name = "chartName") chartName: String,
  ): ChartDataResponseDto {
    val chart = chartService.getChart(
      agencyId = agencyId,
      chart = Chart.getChart(chartName),
    )
    return ChartDataResponseDto(
      agencyId = agencyId,
      chartName = chartName,
      chartEntries = chart,
    )
  }
}