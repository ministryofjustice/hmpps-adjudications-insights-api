package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.config

import com.amazonaws.auth.InstanceProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("!test")
@Configuration
class AwsConfig {

  @Bean
  fun amazonS3(): AmazonS3? {
    return AmazonS3ClientBuilder.standard()
      .withCredentials(InstanceProfileCredentialsProvider(false))
      // .withRegion(Regions.EU_WEST_2)
      //    .withEndpointConfiguration(
      //      AwsClientBuilder.EndpointConfiguration("http://s3.localhost.localstack.cloud:4566", "eu-west-2")
      //    )
      .build()
  }
}