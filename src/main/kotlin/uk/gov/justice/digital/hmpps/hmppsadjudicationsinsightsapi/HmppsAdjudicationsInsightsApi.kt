package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@EnableScheduling
@EnableCaching
@SpringBootApplication
class HmppsAdjudicationsInsightsApi {
  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @Scheduled(cron = "@hourly")
  @CacheEvict(cacheNames = ["chart-meta", "charts"], allEntries = true)
  fun evictCaches() {
    log.info("evicting all caches")
  }
}

fun main(args: Array<String>) {
  runApplication<HmppsAdjudicationsInsightsApi>(*args)
}
