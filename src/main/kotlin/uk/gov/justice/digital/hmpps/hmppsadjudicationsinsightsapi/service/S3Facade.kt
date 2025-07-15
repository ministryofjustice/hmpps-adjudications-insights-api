package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class S3Facade(
  private val amazonS3: AmazonS3,
  @Value("\${data-insights.bucket.name}") private val bucketName: String,
  private val meterRegistry: MeterRegistry,
) {

  fun getFile(fileName: String): String {
    log.info("getting $bucketName $fileName")
    return try {
      amazonS3.getObjectAsString(bucketName, fileName)
    } catch (e: AmazonServiceException) {
      // Increment error counter with detailed tags
      incrementErrorCounter("s3.file.error", fileName, e.errorCode, e.statusCode)

      // Log detailed error information
      log.error("S3 Error retrieving file $fileName from bucket $bucketName: ErrorCode=${e.errorCode ?: "unknown"}, StatusCode=${e.statusCode}, ErrorMessage=${e.errorMessage ?: e.message}, RequestId=${e.requestId ?: "unknown"}")

      // Return empty JSON structure as fallback
      "{\"MDI\": []}"
    }
  }

  fun getS3ObjectMetadata(fileName: String): ObjectMetadata = try {
    amazonS3.getObjectMetadata(bucketName, fileName)
  } catch (e: AmazonServiceException) {
    // Increment error counter with detailed tags
    incrementErrorCounter("s3.metadata.error", fileName, e.errorCode, e.statusCode)

    // Log detailed error information
    log.error("S3 Error retrieving metadata for file $fileName from bucket $bucketName: ErrorCode=${e.errorCode ?: "unknown"}, StatusCode=${e.statusCode}, ErrorMessage=${e.errorMessage ?: e.message}, RequestId=${e.requestId ?: "unknown"}")

    // Return default metadata as fallback
    val defaultMetadata = ObjectMetadata()
    defaultMetadata.lastModified = Date() // Current date as fallback
    defaultMetadata
  }

  /**
   * Increment error counter with detailed tags for monitoring
   */
  private fun incrementErrorCounter(metricName: String, fileName: String, errorCode: String?, statusCode: Int) {
    val tags = listOf(
      Tag.of("file_name", fileName),
      Tag.of("error_code", errorCode ?: "unknown"),
      Tag.of("status_code", statusCode.toString()),
    )

    meterRegistry.counter(metricName, tags).increment()
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}
