package at.obyoxar.jwtverify.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class UserRepository {

    @Autowired
    lateinit var passwordEncoder: BCryptPasswordEncoder

    lateinit var users: MutableList<User>

    @PostConstruct
    fun postConstruct(){
        users = mutableListOf(
                User(passwordEncoder.encode("abc1"), 1, "abc", "google"),
                User(passwordEncoder.encode("123"), 2, "john", "google"),
                User(passwordEncoder.encode("abc3"), 3, "def", "yeet"),
                User(passwordEncoder.encode("admin"), 4, "admin", "yeet")
        ).onEach {
            val role = if(it.username == "admin") UserRole.ADMIN else UserRole.USER
            it.grantRole(role)
        }
    }

    fun findByUsername(username: String): User?{
        println("REPOSITORY: findByUsername: ${username}")
        return users.firstOrNull { it.username == username }
    }

    fun findById(id: Long?): User?{
        println("REPOSITORY: findById: ${id}")
        return users.firstOrNull { it.getUserIdLong() == id }
    }

    fun findByProviderIdAndProviderUserId(providerId: String, providerUserId: String): User?{
        println("REPOSITORY: findByProviderIdAndProviderUserId: ${providerId}, ${providerUserId}")
        return users.firstOrNull {it.providerId == providerId && it.providerUserId == providerUserId}
    }

    fun save(user: User) {
        println("REPOSITORY: save: ${user}")
        users.add(user)
    }

    fun count(): Long {
        return users.size.toLong()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}