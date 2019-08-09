package at.obyoxar.jwtverify.configuration

import com.nimbusds.jose.util.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.ClassPathResource
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.AccessTokenConverter
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import java.nio.charset.Charset

@Configuration
@EnableResourceServer
class OAuth2ResourceServerConfig: ResourceServerConfigurerAdapter() {

    @Autowired
    lateinit var tokenServices: DefaultTokenServices

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources?.tokenServices(tokenServices)
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun tokenStore(): JwtTokenStore {
        return JwtTokenStore(accessTokenConverter())
    }

    @Bean
    @Primary
    fun tokenServices(): DefaultTokenServices {
        val defaultTokenServices = DefaultTokenServices()
        defaultTokenServices.setTokenStore(tokenStore())
//        defaultTokenServices.setTokenEnhancer(accessTokenConverter())
        defaultTokenServices.setSupportRefreshToken(true)
        return defaultTokenServices
    }

    @Bean
    fun accessTokenConverter(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        val res = ClassPathResource("keys/public.txt")
        val publicKey = IOUtils.readInputStreamToString(res.inputStream, Charset.forName("UTF-8"))
        converter.setVerifierKey(publicKey);
        return converter
    }
}

