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
  val chartEntries: List<ChartDataDto>,

)

@Schema(description = "Chart data details")
data class ChartDataDto(

  @Schema(
    description = "Month number, 1-12",
    example = "1",
  )
  val month: Int,

  @Schema(
    description = "Current year",
    example = "2023",
  )
  val year_curr: Int,

  @Schema(
    description = "Previous year",
    example = "2022",
  )
  val year_prev: Int,

  @Schema(
    description = "Value for current year",
    example = "100",
  )
  val count_curr: Int,

  @Schema(
    description = "Value for previous year",
    example = "77",
  )
  val count_prev: Int,

)

enum class Chart(val chartName: String, val fileName: String, val tabName: String, val description: String) {
  CHART_1A("1a", "test_chart_1a.json", "Totals - adjudications and locations", "Total adjudications - over 24 months"),
  CHART_1B("1b", "test_chart_1b.json", "Totals - adjudications and locations", "Total adjudications referred to independent adjudicator – over 24 months"),
  CHART_1C("1c", "test_chart_1c.json", "Totals - adjudications and locations", "Total adjudications by location of rule-breaking offence - last 30 days"),
  CHART_1D("1d", "test_chart_1d.json", "Totals - adjudications and locations", "Total adjudications by location of rule-breaking offence"),

  // missing
  // CHART_1E("1e", "test_chart_1e.json", "Totals - adjudications and locations", "Total adjudications by residential location of offender – last 30 days"),
  CHART_1F("1f", "test_chart_1f.json", "Totals - adjudications and locations", "Total adjudications by residential location of offender"),

  CHART_2A("2a", "test_chart_2a.json", "Protected characteristics and vulnerabilities", "Percentage and number of prisoners in the establishment by {{ religion }} currently"),
  CHART_2B("2b", "test_chart_2b.json", "Protected characteristics and vulnerabilities", "Percentage and number of prisoners with an adjudication by {{ religion }} – last 30 days"),
  CHART_2C("2c", "test_chart_2c.json", "Protected characteristics and vulnerabilities", "Percentage and number of prisoners with an adjudication by {{ religion }} – last 6 months"),
  CHART_2D("2d", "test_chart_2d.json", "Protected characteristics and vulnerabilities", "Offence type by protected characteristic or vulnerability - last 30 days"),
  CHART_2E("2e", "test_chart_2e.json", "Protected characteristics and vulnerabilities", "Punishment by protected characteristic or vulnerability - last 30 days"),
  CHART_2F("2f", "test_chart_2f.json", "Protected characteristics and vulnerabilities", "Plea by protected characteristic or vulnerability - last 30 days"),
  CHART_2G("2g", "test_chart_2g.json", "Protected characteristics and vulnerabilities", "Finding by protected characteristic or vulnerability - last 30 days"),

  CHART_3A("3a", "test_chart_3a.json", "Offence type", "Total adjudications by offence type – current month and previous 12 months"),
  CHART_3B("3b", "test_chart_3b.json", "Offence type", "Offence type by location – last 30 days"),

  CHART_4A("4a", "test_chart_4a.json", "Punishments", "Total adjudications by punishment – current month and previous 12 months"),
  CHART_4B("4b", "test_chart_4b.json", "Punishments", "Total adjudications for each offence type broken down by punishment - current month and previous 12 months"),
  CHART_4C("4c", "test_chart_4c.json", "Punishments", "Percentage of suspended and activated punishments - current month and last 12 months"),

  CHART_5A("5a", "test_chart_5a.json", "Pleas and findings", "Total adjudications by plea – current month and previous 12 months"),
  CHART_5B("5b", "test_chart_5b.json", "Pleas and findings", "Total adjudications by finding – current month and previous 12 months"),
  // missing
  // CHART_5C("5c", "test_chart_5c.json", "Pleas and findings", "Percentage of resolved adjudications with more than one hearing – current month and last 12 months"),
  ;
  companion object {
    fun getChart(chartName: String) = Chart.values().first { it.chartName == chartName }
  }
}
