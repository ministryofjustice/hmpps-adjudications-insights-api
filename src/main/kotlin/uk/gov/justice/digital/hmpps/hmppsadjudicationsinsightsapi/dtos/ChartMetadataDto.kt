package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Chart information of the file stored on S3 bucket ")
data class ChartMetadataDto (
  @Schema(
    description = "Chart name",
    example = "1a",
  )
  val chartName: String,
  @Schema(
    description = "Last Modified Date",
    example = "2023-08-16 15:23:47",
  )
  val lastModifiedDate: LocalDateTime
)