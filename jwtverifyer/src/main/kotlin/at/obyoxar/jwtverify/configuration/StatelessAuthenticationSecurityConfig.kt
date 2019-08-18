package at.obyoxar.jwtverify.configuration

import at.obyoxar.jwtverify.lib.OJwtDecoderBase
import at.obyoxar.jwtverify.lib.OSocialConfigurer
import at.obyoxar.jwtverify.lib.OpenIdConnectFilter
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.PasswordLookup
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.JWSKeySelector
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.*
import org.springframework.core.annotation.Order
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.ObjectPostProcessor
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import org.springframework.social.UserIdSource
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.google.api.Google
import org.springframework.social.security.SocialAuthenticationFilter
import org.springframework.social.security.SpringSocialConfigurer
import java.security.Key
import java.security.KeyStore

@Configuration
@Order(2)
class StatelessAuthenticationSecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var passwordEncoder: BCryptPasswordEncoder

    @Autowired
    lateinit var authenticationSuccessHandler: SocialAuthenticationSuccessHandler

    @Autowired
    lateinit var statelessAuthenticationFilter: StatelessAuthenticationFilter

    @Autowired
    lateinit var userIdSource: UserIdSource

    @Autowired
    lateinit var userService: SocialUserService

    override fun configure(http: HttpSecurity?) {
        val socialConfigurer = SpringSocialConfigurer()
        socialConfigurer.addObjectPostProcessor(object: ObjectPostProcessor<SocialAuthenticationFilter> {
            override fun <O : SocialAuthenticationFilter?> postProcess(socialAuthenticationFilter: O): O {
                socialAuthenticationFilter!!.setAuthenticationSuccessHandler(authenticationSuccessHandler)
                return socialAuthenticationFilter
            }
        })

        http!!
                .exceptionHandling().and().anonymous().and().servletApi().and().headers().cacheControl().and().and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/verify").permitAll()
                .antMatchers("/oauth/token/revokeById/**").permitAll()
                .antMatchers("/**/favicon.ico").permitAll()
                .antMatchers("/tokens/**").permitAll()

                //Copied from GitHub
                .antMatchers("/").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/users/current/details").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/user").hasRole("USER")
                .anyRequest().hasRole("USER")
                .and()
                .addFilterBefore(statelessAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter::class.java)
                .apply(socialConfigurer.userIdSource(userIdSource))

//                .anyRequest().permitAll()
//                .and().formLogin().permitAll()
//                .and().csrf().disable()
//                .apply(OSocialConfigurer())
//                .setSuccessHandler(authenticationSuccessHandler)
//                .and()

    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService)
    }

    override fun userDetailsService(): SocialUserService {
        return userService
    }


//    @Bean
//    fun jwtDecoder(): JwtDecoder {
//        val res = javaClass.getResource("/keys/obykeys.jks")
//        val keyStore = KeyStore.getInstance("JKS")
//        keyStore.load(res.openStream(), "obypass".toCharArray())
//        val pwLookup = PasswordLookup { "obypass".toCharArray() }
//        val jwkLoadedSet = JWKSet.load(keyStore, null)
//        val jwkSet = ImmutableJWKSet<SecurityContext>(jwkLoadedSet)
//
//        val rsakey = RSAKey.load(keyStore, "obykey", "obypass".toCharArray())
//
//
//        val keyStoreFactory = KeyStoreKeyFactory(ClassPathResource("keys/obykeys.jks"), "obypass".toCharArray())
//        val keyPair = keyStoreFactory.getKeyPair("obykey")
//
//        val selector = JWSKeySelector<SecurityContext> { header, context -> mutableListOf(keyPair.public as Key) }
//        val source = JWKSource<SecurityContext> { jwkSelector, _ -> mutableListOf(rsakey as JWK) }
//        return OJwtDecoderBase(source, "RS256")
//    }
//
//    override fun configure(auth: AuthenticationManagerBuilder?) {
//        auth!!
//                .inMemoryAuthentication()
//                .withUser("john").password(passwordEncoder.encode("123")).roles("USER").and()
//                .withUser("tom").password(passwordEncoder.encode("111")).roles("ADMIN").and()
//                .withUser("user1").password(passwordEncoder.encode("pass")).roles("USER").and()
//                .withUser("admin").password(passwordEncoder.encode("nimda")).roles("ADMIN");
//    }
//
//
//    @Bean
//    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
//    fun google(repo: ConnectionRepository): Google{
//        val connection = repo.findPrimaryConnection(Google::class.java)
//        return connection.api
//    }
}

