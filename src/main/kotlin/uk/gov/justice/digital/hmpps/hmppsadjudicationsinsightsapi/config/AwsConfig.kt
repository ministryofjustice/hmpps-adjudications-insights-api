package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.config

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("!test")
@Configuration
class AwsConfig {

  @Bean
  fun amazonS3(
    @Value("\${data-insights.bucket.region}") region: String,
  ): AmazonS3? = AmazonS3ClientBuilder.standard().withCredentials(
    DefaultAWSCredentialsProviderChain(),
  ).withRegion(region).build()
}
