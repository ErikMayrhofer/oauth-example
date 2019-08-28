package at.obyoxar.jwtverify.meta

import at.obyoxar.jwtverify.configuration.User
import org.springframework.security.core.context.SecurityContextHolder
import java.io.Serializable

data class AuthorizationServerInfo(
        val name: String,
        val version: String,
        val loggedInAs: User? = null
): Serializable {

    fun decorateWithLoginData(): AuthorizationServerInfo{
        val user = SecurityContextHolder.getContext().authentication.details as? User
        return AuthorizationServerInfo(name, version, user)
    }
}