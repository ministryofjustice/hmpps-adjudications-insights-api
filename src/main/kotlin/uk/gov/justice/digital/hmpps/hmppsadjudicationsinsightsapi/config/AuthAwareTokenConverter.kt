package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.config

import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter

class AuthAwareTokenConverter : Converter<Jwt, AbstractAuthenticationToken> {
  private val logger = LoggerFactory.getLogger(AuthAwareTokenConverter::class.java)
  init {
    logger.info("AuthAwareTokenConverter INITIALISED")
  }

  private val jwtGrantedAuthoritiesConverter: Converter<Jwt, Collection<GrantedAuthority>> =
    JwtGrantedAuthoritiesConverter()

  override fun convert(jwt: Jwt): AbstractAuthenticationToken {
    val claims = jwt.claims
    val userName = findUserName(claims)
    val clientId = findClientId(claims)
    val authorities = extractAuthorities(jwt)

    logger.debug("Authorities extracted: {}", authorities)

    return AuthAwareAuthenticationToken(jwt, userName, clientId, authorities)
  }

  private fun findUserName(claims: Map<String, Any?>): String? = if (claims.containsKey("user_name")) claims["user_name"] as String else null

  private fun findClientId(claims: Map<String, Any?>): String? = claims["client_id"] as? String

  private fun extractAuthorities(jwt: Jwt): Collection<GrantedAuthority> {
    val authorities = mutableSetOf<GrantedAuthority>()

    jwtGrantedAuthoritiesConverter.convert(jwt).let(authorities::addAll)

    (jwt.claims["authorities"] as? Collection<*>)?.filterIsInstance<String>()
      ?.mapTo(authorities, ::SimpleGrantedAuthority)

    (jwt.claims["roles"] as? Collection<*>)?.filterIsInstance<String>()
      ?.map { it.ensureRolePrefix() }
      ?.mapTo(authorities, ::SimpleGrantedAuthority)

    return authorities.toSet()
  }
}

private fun String.ensureRolePrefix(): String =
  if (startsWith("ROLE_")) this else "ROLE_$this"

class AuthAwareAuthenticationToken(
  jwt: Jwt,
  val userName: String?,
  val clientId: String?,
  authorities: Collection<GrantedAuthority>,
) : JwtAuthenticationToken(jwt, authorities) {
  override fun getPrincipal(): String? = userName ?: clientId
}
