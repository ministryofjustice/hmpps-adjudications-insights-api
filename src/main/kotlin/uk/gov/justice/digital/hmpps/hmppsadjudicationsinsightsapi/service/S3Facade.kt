package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.amazonaws.services.s3.AmazonS3
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class S3Facade(
  private val amazonS3: AmazonS3,
  @Value("\${data-insights.bucket.name}") private val bucketName: String,
) {

  fun getFile(fileName: String): String {
    return amazonS3.getObjectAsString(bucketName, fileName)
  }
}
