package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.config

import com.amazonaws.ClientConfiguration
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("test", "local")
@Configuration
class AwsConfigLocalstack {

  @Bean
  fun amazonS3(): AmazonS3? {
    // Disable chunked encoding to avoid sending x-amz-trailer header
    val clientConfiguration = ClientConfiguration()

    return AmazonS3ClientBuilder.standard()
      .withEndpointConfiguration(
        AwsClientBuilder.EndpointConfiguration("http://s3.localhost.localstack.cloud:4566", "eu-west-1"),
      )
      .withClientConfiguration(clientConfiguration)
      .withPathStyleAccessEnabled(true) // recommended for LocalStack
      .disableChunkedEncoding() // disable chunked encoding
      .build()
  }
}
