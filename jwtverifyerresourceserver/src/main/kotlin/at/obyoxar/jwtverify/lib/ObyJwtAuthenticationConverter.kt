package at.obyoxar.jwtverify.lib

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.core.authority.SimpleGrantedAuthority

private val WELL_KNOWN_SCOPE_ATTRIBUTE_NAMES = arrayOf("scope", "scp", "authorities") // added authorities

class ObyJwtAuthenticationConverter: JwtAuthenticationConverter(), Converter<Jwt, AbstractAuthenticationToken> {
    override fun extractAuthorities(jwt: Jwt): Collection<GrantedAuthority> {
        return this.getScopes(jwt).map { SimpleGrantedAuthority(it) }
    }

    private fun getScopes(jwt: Jwt): Collection<String> {
        return WELL_KNOWN_SCOPE_ATTRIBUTE_NAMES.flatMap {
            (jwt.claims[it] as? String)?.split(" ")?.filter { s -> s.isNotBlank() } ?: listOf()
        }
    }
}