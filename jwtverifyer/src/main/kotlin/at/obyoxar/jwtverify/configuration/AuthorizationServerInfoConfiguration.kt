package at.obyoxar.jwtverify.configuration

import at.obyoxar.jwtverify.meta.AuthorizationServerInfo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Configuration
class AuthorizationServerInfoConfiguration {
    @Bean
    fun authorizationServerInfo(): AuthorizationServerInfo{
        return AuthorizationServerInfo("ObyTestAuthServer", "0.0.1")
    }
}