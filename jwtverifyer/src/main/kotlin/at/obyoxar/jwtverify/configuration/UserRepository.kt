package at.obyoxar.jwtverify.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.social.connect.Connection
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
                User(passwordEncoder.encode("123"), 2, "john", "local"),
                User(passwordEncoder.encode("admin"), 4, "admin", "local"),
                User(passwordEncoder.encode("erik"), 5, "erik", "google", "114861104432965613969")
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

    fun findByProviderIdAndProviderUserId(providerId: String, providerUserId: String): List<User> {
        println("REPOSITORY: findByProviderIdAndProviderUserId: ${providerId}, ${providerUserId}")
        return users.filter {it.providerId == providerId && it.providerUserId == providerUserId}
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

    fun findUserIdsConnectedTo(providerId: String, providerUserIds: MutableSet<String>): MutableSet<String> {
        println("REPOSITORY: findUserIdsConnectedTo $providerId, $providerUserIds")
        return users.filter { it.providerId == providerId && it.providerUserId in providerUserIds }.map { it.userId }.toMutableSet()
    }

    fun findUserIdsWithConnection(connection: Connection<*>): MutableList<String> {
        println("REPOSITORY: findUserIdsWithConnection $connection")
        return findByProviderIdAndProviderUserId(connection.key.providerId, connection.key.providerUserId).map { it.userId }.toMutableList()
    }
}