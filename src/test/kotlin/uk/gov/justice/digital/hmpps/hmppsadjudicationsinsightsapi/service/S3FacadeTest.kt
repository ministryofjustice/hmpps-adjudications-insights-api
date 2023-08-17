package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.text.SimpleDateFormat

class S3FacadeTest {

  private val amazonS3: AmazonS3 = mock()

  @Test
  fun testChartJson() {
    val bucketName = "some-bucket-name"
    val fileName = "1a.json"

    whenever(amazonS3.getObjectAsString(eq(bucketName), eq(fileName))).thenReturn("<file content>")

    val s3Facade = S3Facade(amazonS3, bucketName)

    val chart = s3Facade.getFile(fileName)
    assertThat(chart).isEqualTo("<file content>")
  }

  @Test
  fun testS3ObjectMetadata() {
    val bucketName = "some-bucket-name"
    val fileName = "1a.json"

    val dateStr = "2023-08-15 14:11:53"
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val objectMetadata = ObjectMetadata()
    val modifiedDate = sdf.parse(dateStr)
    objectMetadata.lastModified = modifiedDate
    whenever(amazonS3.getObjectMetadata(eq(bucketName), eq(fileName))).thenReturn(objectMetadata)

    val s3Facade = S3Facade(amazonS3, bucketName)

    val metadata = s3Facade.getS3ObjectMetadata(fileName)
    assertThat(metadata.lastModified).isEqualTo(modifiedDate)
  }
}
