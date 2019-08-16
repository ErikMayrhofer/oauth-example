package at.obyoxar.jwtverify.configuration

import at.obyoxar.jwtverify.lib.OJwtDecoderBase
import at.obyoxar.jwtverify.lib.ObyJwtAuthenticationConverter
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.PasswordLookup
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.ImmutableSecret
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.JWSKeySelector
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.io.ClassPathResource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.File
import java.security.Key
import java.security.KeyException
import java.security.KeyStore
import javax.crypto.SecretKey

@Configuration
@Order(2)
class WebSecurityConfig: WebSecurityConfigurerAdapter() {

    @Override
    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(http: HttpSecurity?) {
        http!!.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .oauth2ResourceServer()
                    .jwt().decoder(jwtDecoder()).jwtAuthenticationConverter(converter())
                    .and()
                .and()
                .authorizeRequests()
                .antMatchers("/user2").hasAnyRole("ADMIN")
                .antMatchers("/**").permitAll()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val keyStore = KeyStore.getInstance("JKS")
        keyStore.load(ClassPathResource("keys/obykeys.jks").inputStream, "obypass".toCharArray())

        val rsakey = RSAKey.load(keyStore, "obykey", "obypass".toCharArray())

        val source = JWKSource<SecurityContext> { _, _ ->
            mutableListOf(rsakey as JWK)
        }
        return OJwtDecoderBase(source, "RS256")
    }

    @Bean
    fun converter(): ObyJwtAuthenticationConverter {
        return ObyJwtAuthenticationConverter()
    }
}