package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.jwt.Jwt

class AuthAwareTokenConverterTest {
  private val converter = AuthAwareTokenConverter()

  @Test
  fun `uses username as principal when present`() {
    val jwt = jwt("user_name" to "USER", "client_id" to "CLIENT")

    val token = converter.convert(jwt)

    assertThat(token.principal).isEqualTo("USER")
  }

  @Test
  fun `uses client id as principal when username is absent`() {
    val jwt = jwt("client_id" to "CLIENT")

    val token = converter.convert(jwt)

    assertThat(token.principal).isEqualTo("CLIENT")
  }

  @Test
  fun `uses the default JWT principal when username and client id are absent`() {
    val jwt = jwt("sub" to "SUBJECT")

    val token = converter.convert(jwt)

    assertThat(token.principal).isSameAs(jwt)
  }

  private fun jwt(vararg claims: Pair<String, Any>): Jwt = Jwt.withTokenValue("token")
    .header("alg", "none")
    .claims { claimSet -> claims.forEach { (name, value) -> claimSet[name] = value } }
    .build()
}
