package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.data

 data class FileData(
  val incident_prison: String,
  val discovery_month: String,
  val discovery_year_curr: String,
  val count_curr: String,
  val discovery_year_prev: String,
  val count_prev: String,
)

data class FileDataAll(
  val fileData: List<FileData>
)

//{"incident_prison":"SKI","discovery_month":6,"discovery_year_curr":2022,"count_curr":16,"discovery_year_prev":2021,"count_prev":38}

