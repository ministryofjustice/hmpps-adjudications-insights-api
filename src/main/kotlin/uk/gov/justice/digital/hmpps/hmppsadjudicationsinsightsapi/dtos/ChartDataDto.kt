package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Details of a chart data entry")
data class ChartDataResponseDto(
  @Schema(
    description = "Prison agency ID",
    example = "ACI",
  )
  val agencyId: String,

  @Schema(
    description = "Chart name",
    example = "1a",
  )
  val chartName: String,

  @Schema(
    description = "List of chart data details",
  )
  val chartEntries: List<Map<String, Any>>,
)

enum class Chart(val chartName: String, val fileName: String, val tabName: String, val description: String) {
  CHART_1A("1a", "chart/1a.json", "Totals - adjudications and locations", "Adjudication reports created - over 24 months"),
  CHART_1B("1b", "chart/1b.json", "Totals - adjudications and locations", "Adjudication reports referred to independent adjudicator – over 24 months"),
  CHART_1C("1c", "chart/1c.json", "Totals - adjudications and locations", "Number of people placed on report in the past 30 days"),
  CHART_1D("1d", "chart/1d.json", "Totals - adjudications and locations", "Adjudication reports by location of adjudication incident – last 30 days"),
  CHART_1F("1f", "chart/1f.json", "Totals - adjudications and locations", "Adjudication reports by residential location of prisoner – last 30 days"),

  CHART_2A("2a", "chart/2a.json", "Protected characteristics and vulnerabilities", "Overview of prisoners in the establishment currently"),
  CHART_2B("2b", "chart/2b.json", "Protected characteristics and vulnerabilities", "Adjudication reports by protected or responsivity characteristic – last 30 days"),
  CHART_2D("2d", "chart/2d.json", "Protected characteristics and vulnerabilities", "Adjudication offence type by protected or responsivity characteristic - last 30 days"),
  CHART_2E("2e", "chart/2e.json", "Protected characteristics and vulnerabilities", "Punishment by protected or responsivity characteristic - last 30 days"),
  CHART_2F("2f", "chart/2f.json", "Protected characteristics and vulnerabilities", "Plea by protected or responsivity characteristic - last 30 days"),
  CHART_2G("2g", "chart/2g.json", "Protected characteristics and vulnerabilities", "Finding by protected or responsivity characteristic - last 30 days"),

  CHART_3A("3a", "chart/3a.json", "Offence type", "Adjudication offence types – current month and previous 12 months"),
  CHART_3B("3b", "chart/3b.json", "Offence type", "Adjudication offence type by location – last 30 days"),

  CHART_4A("4a", "chart/4a.json", "Punishments", "Punishments given – current month and previous 12 months"),
  CHART_4B("4b", "chart/4b.json", "Punishments", "Punishments given for each adjudication offence type - current month and previous 12 months"),
  CHART_4C("4c", "chart/4c.json", "Punishments", "Suspended and activated punishments - current month and last 12 months"),
  CHART_4D("4d", "chart/4d.json", "Punishments", "Most commonly used punishments last month"),

  CHART_5A("5a", "chart/5a.json", "Pleas and findings", "Pleas given – current month and previous 12 months"),
  CHART_5B("5b", "chart/5b.json", "Pleas and findings", "Findings – current month and previous 12 months"),
  CHART_5C("5c", "chart/5c.json", "Pleas and findings", "Adjudications resolved with more than one hearing – current month and previous 12 months "),
  ;

  companion object {
    fun getChart(chartName: String) = entries.first { it.chartName == chartName }
  }
}
