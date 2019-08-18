package at.obyoxar.jwtverify.configuration

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.social.UserIdSource

class UserAuthenticationUserIdSource: UserIdSource {
//    override fun getUserId(): String {
//        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
//        return jwt.id
//    }

    override fun getUserId(): String{
        val authentication = SecurityContextHolder.getContext().authentication
        var user: User? = null
        if (authentication is UserAuthentication) {
            user = authentication.principal as User
        }

        if (user == null) {
            throw IllegalStateException("Unable to get a ConnectionRepository: no user signed in")
        }
        return user.userId
    }

}