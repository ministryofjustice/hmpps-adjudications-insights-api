package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.integration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.utils.JwtAuthHelper

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
@ActiveProfiles("test")
abstract class IntegrationTestBase {

  @Suppress("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  lateinit var webTestClient: WebTestClient

  @Autowired
  lateinit var jwtAuthHelper: JwtAuthHelper

  fun setHeaders(
    contentType: MediaType = MediaType.APPLICATION_JSON,
    username: String? = "ITAG_USER",
    roles: List<String> = listOf("ROLE_VIEW_ADJUDICATIONS"),
  ): (HttpHeaders) -> Unit = {
    it.setBearerAuth(jwtAuthHelper.createJwt(subject = username, roles = roles))
    it.contentType = contentType
  }
}
