package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.amazonaws.services.s3.AmazonS3
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class S3FacadeTest {

  private var amazonS3: AmazonS3 = mock()

  @Test
  fun testChartJson() {
    val bucketName = "some-bucket-name"
    val fileName = "1a_test.json"

    whenever(amazonS3.getObjectAsString(eq(bucketName), eq(fileName))).thenReturn("<file content>")

    val s3Facade = S3Facade(amazonS3, bucketName)

    val chart = s3Facade.getFile(fileName)
    assertThat(chart).isEqualTo("<file content>")
  }
}
