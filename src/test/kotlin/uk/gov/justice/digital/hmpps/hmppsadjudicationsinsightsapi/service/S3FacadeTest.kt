package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.AmazonS3Exception
import com.amazonaws.services.s3.model.ObjectMetadata
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Tag
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.text.SimpleDateFormat

class S3FacadeTest {

  private val amazonS3: AmazonS3 = mock()
  private val meterRegistry: MeterRegistry = mock()
  private val mockCounter: Counter = mock()

  @Test
  fun testChartJson() {
    val bucketName = "some-bucket-name"
    val fileName = "1a.json"

    whenever(amazonS3.getObjectAsString(eq(bucketName), eq(fileName))).thenReturn("<file content>")

    val s3Facade = S3Facade(amazonS3, bucketName, meterRegistry)

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

    val s3Facade = S3Facade(amazonS3, bucketName, meterRegistry)

    val metadata = s3Facade.getS3ObjectMetadata(fileName)
    assertThat(metadata.lastModified).isEqualTo(modifiedDate)
  }

  @Test
  fun testChartJsonHandlesException() {
    val bucketName = "some-bucket-name"
    val fileName = "1a.json"

    whenever(amazonS3.getObjectAsString(eq(bucketName), eq(fileName))).thenThrow(AmazonS3Exception("Not found"))
    whenever(meterRegistry.counter(any(), any<Iterable<Tag>>())).thenReturn(mockCounter)

    val s3Facade = S3Facade(amazonS3, bucketName, meterRegistry)

    val result = s3Facade.getFile(fileName)
    // Should return default empty JSON structure
    assertThat(result).isEqualTo("{\"MDI\": []}")

    verify(amazonS3, times(1)).getObjectAsString(bucketName, fileName)
  }

  @Test
  fun testS3ObjectMetadataHandlesException() {
    val bucketName = "some-bucket-name"
    val fileName = "1a.json"

    whenever(amazonS3.getObjectMetadata(eq(bucketName), eq(fileName))).thenThrow(AmazonS3Exception("Not found"))
    whenever(meterRegistry.counter(any(), any<Iterable<Tag>>())).thenReturn(mockCounter)

    val s3Facade = S3Facade(amazonS3, bucketName, meterRegistry)

    val result = s3Facade.getS3ObjectMetadata(fileName)
    // Should return default ObjectMetadata
    assertThat(result).isNotNull
    assertThat(result.lastModified).isNotNull

    verify(amazonS3, times(1)).getObjectMetadata(bucketName, fileName)
  }
}
