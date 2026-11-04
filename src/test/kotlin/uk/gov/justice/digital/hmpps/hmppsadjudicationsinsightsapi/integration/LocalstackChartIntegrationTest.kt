package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.integration

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CreateBucketRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.dtos.ChartMetadataDto
import java.time.ZoneId
import java.util.UUID

class LocalstackChartIntegrationTest : IntegrationTestBase() {
  @Autowired
  private lateinit var amazonS3: AmazonS3

  @Test
  fun `chart data and metadata are read from S3`() {
    val key = "chart/1a.json"
    amazonS3.createBucket(CreateBucketRequest(bucketName, "eu-west-1"))
    try {
      amazonS3.putObject(bucketName, key, """{"MDI":[{"value":"localstack test"}]}""")
      val lastModified = amazonS3.getObjectMetadata(bucketName, key).lastModified.toInstant()
        .atZone(ZoneId.systemDefault()).toLocalDateTime()

      webTestClient.get()
        .uri("/api/data-insights/chart/MDI/1a")
        .headers(setHeaders())
        .exchange()
        .expectStatus().isOk
        .expectBody()
        .jsonPath("$.chartEntries[0].value").isEqualTo("localstack test")

      val metadata = webTestClient.get()
        .uri("/api/data-insights/chart/last-updated/1a")
        .headers(setHeaders())
        .exchange()
        .expectStatus().isOk
        .expectBody(ChartMetadataDto::class.java)
        .returnResult().responseBody!!
      assertThat(metadata.lastModifiedDate).isEqualTo(lastModified)
    } finally {
      amazonS3.deleteObject(bucketName, key)
      amazonS3.deleteBucket(bucketName)
    }
  }

  companion object {
    private val bucketName = "insights-test-${UUID.randomUUID()}"

    @JvmStatic
    @DynamicPropertySource
    fun properties(registry: DynamicPropertyRegistry) {
      registry.add("data-insights.bucket.name") { bucketName }
    }
  }
}
