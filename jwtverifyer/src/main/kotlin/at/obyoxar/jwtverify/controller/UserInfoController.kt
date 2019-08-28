package at.obyoxar.jwtverify.controller

import at.obyoxar.jwtverify.configuration.User
import at.obyoxar.jwtverify.configuration.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.social.google.api.Google
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/info")
class UserInfoController {

    @Autowired
    lateinit var repo: UserRepository

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    fun me(): User{
        val details = SecurityContextHolder.getContext().authentication.details
        return details as User
    }

}