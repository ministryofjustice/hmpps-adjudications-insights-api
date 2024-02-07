package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service.ChartService

@EnableScheduling
@EnableCaching
@SpringBootApplication
class HmppsAdjudicationsInsightsApi

fun main(args: Array<String>) {
  runApplication<HmppsAdjudicationsInsightsApi>(*args)
}

@Scheduled(cron = "@hourly")
@CacheEvict(cacheNames = ["chart-meta", "charts"], allEntries = true)
fun evictCaches() {
  ChartService.log.info("evicting all caches")
}
