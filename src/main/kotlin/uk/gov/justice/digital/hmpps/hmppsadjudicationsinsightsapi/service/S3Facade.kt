package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class S3Facade(
  private val amazonS3: AmazonS3,
  @Value("\${data-insights.bucket.name}") private val bucketName: String,
) {

  fun getFile(fileName: String): String {
    log.info("getting $bucketName $fileName")
    return amazonS3.getObjectAsString(bucketName, fileName)
  }

  fun getS3ObjectMetadata(fileName: String): ObjectMetadata = amazonS3.getObjectMetadata(bucketName, fileName)

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}
