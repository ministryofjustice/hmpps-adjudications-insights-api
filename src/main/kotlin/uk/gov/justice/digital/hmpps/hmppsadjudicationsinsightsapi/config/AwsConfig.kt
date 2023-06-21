package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.config

import com.amazonaws.auth.InstanceProfileCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsConfig {

  @Bean
  fun amazonS3(): AmazonS3? {
    return AmazonS3ClientBuilder.standard()
      .withCredentials(InstanceProfileCredentialsProvider(false))
      .withRegion(Regions.EU_WEST_2)
      .build()
  }
}
