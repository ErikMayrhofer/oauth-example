package at.obyoxar.jwtverify.configuration

import at.obyoxar.jwtverify.lib.OJwtDecoderBase
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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import java.io.File
import java.security.Key
import java.security.KeyException
import java.security.KeyStore
import javax.crypto.SecretKey

@Configuration
@Order(2)
class WebSecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var passwordEncoder: BCryptPasswordEncoder

    @Override
    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

//      TODO NEEDED FOR GOOGLE
//    override fun configure(web: WebSecurity?) {
//        web!!.ignoring()
//                .antMatchers("/verify")
//    }

    override fun configure(http: HttpSecurity?) {
        http!!
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/oauth/token/revokeById/**").permitAll()
                .antMatchers("/**/favicon.ico").permitAll()
                .antMatchers("/tokens/**").permitAll()
                .anyRequest().permitAll()
                .and().formLogin().permitAll()
                .and().csrf().disable()

    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val res = javaClass.getResource("/keys/obykeys.jks")
        val keyStore = KeyStore.getInstance("JKS")
        keyStore.load(res.openStream(), "obypass".toCharArray())
        val pwLookup = PasswordLookup { "obypass".toCharArray() }
        val jwkLoadedSet = JWKSet.load(keyStore, null)
        val jwkSet = ImmutableJWKSet<SecurityContext>(jwkLoadedSet)

        val rsakey = RSAKey.load(keyStore, "obykey", "obypass".toCharArray())


        val keyStoreFactory = KeyStoreKeyFactory(ClassPathResource("keys/obykeys.jks"), "obypass".toCharArray())
        val keyPair = keyStoreFactory.getKeyPair("obykey")

        val selector = JWSKeySelector<SecurityContext> { header, context -> mutableListOf(keyPair.public as Key) }
        val source = JWKSource<SecurityContext> { jwkSelector, _ -> mutableListOf(rsakey as JWK) }
        return OJwtDecoderBase(source, "RS256")
    }

//    @Bean
//    fun authenticationFilter(): Filter {
//
//    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!
                .inMemoryAuthentication()
                .withUser("john").password(passwordEncoder.encode("123")).roles("USER").and()
                .withUser("tom").password(passwordEncoder.encode("111")).roles("ADMIN").and()
                .withUser("user1").password(passwordEncoder.encode("pass")).roles("USER").and()
                .withUser("admin").password(passwordEncoder.encode("nimda")).roles("ADMIN");
    }
}