package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableCaching
@SpringBootApplication()
class HmppsAdjudicationsInsightsApi

fun main(args: Array<String>) {
  runApplication<HmppsAdjudicationsInsightsApi>(*args)
}
