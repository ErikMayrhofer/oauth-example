package at.obyoxar.jwtverify.lib

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.core.authority.SimpleGrantedAuthority



class ObyJwtAuthenticationConverter: JwtAuthenticationConverter(), Converter<Jwt, AbstractAuthenticationToken> {
//    override fun extractAuthorities(jwt: Jwt?): MutableCollection<GrantedAuthority> {
//        return super.extractAuthorities(jwt)
//    }


    private val SCOPE_AUTHORITY_PREFIX = "SCOPE_"

    private val WELL_KNOWN_SCOPE_ATTRIBUTE_NAMES = arrayOf("scope", "scp", "authorities") // added authorities


    override fun extractAuthorities(jwt: Jwt): Collection<GrantedAuthority> {
        return this.getScopes(jwt)
                .map { SimpleGrantedAuthority(it) }
    }

    //TODO Kotlinify
    private fun getScopes(jwt: Jwt): Collection<String> {
        val authorities = mutableListOf<String>()
        // add to collection instead of returning early
        for (attributeName in WELL_KNOWN_SCOPE_ATTRIBUTE_NAMES) {
            val scopes = jwt.claims[attributeName]
            if (scopes is String) {
                if (scopes.isNotEmpty()) {
                    authorities.addAll(scopes.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                }
            } else if (scopes is Collection<*>) {
                authorities.addAll(scopes as Collection<String>)
            }
        }

        return authorities
    }
}