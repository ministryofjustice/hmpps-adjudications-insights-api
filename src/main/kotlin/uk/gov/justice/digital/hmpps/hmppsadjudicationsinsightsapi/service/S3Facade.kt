package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class S3Facade(
  private val amazonS3: AmazonS3,
  @Value("\${data-insights.bucket.name}") private val bucketName: String,
) {

  @Cacheable("insights")
  fun getFile(fileName: String): String {
    log.info("getting $bucketName $fileName")
    return amazonS3.getObjectAsString(bucketName, fileName)
  }

  @Cacheable("insights-meta")
  fun getS3ObjectMetadata(fileName: String): ObjectMetadata {
    return amazonS3.getObjectMetadata(bucketName, fileName)
  }

  @Scheduled(cron = "@hourly")
  private fun evictCaches() {
    evict()
  }

  @CacheEvict(value = ["insights", "insights-meta"], allEntries = true)
  private fun evict() {
    log.info("caches evicted")
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}
