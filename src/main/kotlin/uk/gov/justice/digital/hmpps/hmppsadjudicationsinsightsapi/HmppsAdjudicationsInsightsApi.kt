package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication()
class HmppsAdjudicationsInsightsApi

fun main(args: Array<String>) {
  runApplication<HmppsAdjudicationsInsightsApi>(*args)
}
