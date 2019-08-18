package at.obyoxar.jwtverify.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.core.env.Environment
import org.springframework.social.UserIdSource
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer
import org.springframework.social.config.annotation.EnableSocial
import org.springframework.social.config.annotation.SocialConfigurerAdapter
import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository
import org.springframework.social.github.api.GitHub
import org.springframework.social.github.connect.GitHubConnectionFactory
import org.springframework.social.google.api.Google
import org.springframework.social.google.connect.GoogleConnectionFactory

@Configuration
@EnableSocial
class StatelessSocialConfig: SocialConfigurerAdapter() {

    @Value("\${google.clientId}")
    lateinit var clientId: String

    @Value("\${google.clientSecret}")
    lateinit var clientSecret: String

    @Autowired
    lateinit var autoSignUpHandler: AutoSignUpHandler

    @Autowired
    lateinit var userService: UserService

    override fun addConnectionFactories(connectionFactoryConfigurer: ConnectionFactoryConfigurer?, environment: Environment?) {
        connectionFactoryConfigurer!!.addConnectionFactory(
            GoogleConnectionFactory(clientId, clientSecret).apply {
                scope = "email openid profile"
            }
        )
        connectionFactoryConfigurer.addConnectionFactory(
                GitHubConnectionFactory("571f3887642dc9de2883", "3abb3c5015a5b06f16fa88854e8f24b4aecfa596")
        )
    }

    override fun getUserIdSource(): UserIdSource {
        return UserAuthenticationUserIdSource()
    }

    override fun getUsersConnectionRepository(connectionFactoryLocator: ConnectionFactoryLocator): UsersConnectionRepository {
//        val usersConnectionRepository = InMemoryUsersConnectionRepository(connectionFactoryLocator)
//        usersConnectionRepository.setConnectionSignUp(autoSignUpHandler)
//        usersConnectionRepository.
        val usersConnectionRepository = SimpleUsersConnectionRepository(userService, connectionFactoryLocator, autoSignUpHandler)
        return usersConnectionRepository
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    fun google(repo: ConnectionRepository): Google{
        val conn = repo.findPrimaryConnection(Google::class.java);
        return conn.api
    }
}

