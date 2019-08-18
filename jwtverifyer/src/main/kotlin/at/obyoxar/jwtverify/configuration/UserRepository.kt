package at.obyoxar.jwtverify.configuration

import org.springframework.stereotype.Component

@Component
class UserRepository {

    val users = mutableListOf(
            User("abc1", 1, "abc", "google"),
            User("ABC1", 2, "abc", "google"),
            User("def1", 3, "def", "yeet")
    )

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
        users.add(user)
    }

    fun count(): Long {
        return users.size.toLong()
    }
}