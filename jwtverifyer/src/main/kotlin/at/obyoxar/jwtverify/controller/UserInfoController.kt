package at.obyoxar.jwtverify.controller

import at.obyoxar.jwtverify.configuration.User
import at.obyoxar.jwtverify.configuration.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.social.google.api.Google
import org.springframework.social.google.api.plus.Person
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserInfoController {

    @Autowired
    lateinit var google: Google

    @Autowired
    lateinit var repo: UserRepository

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getInfo(): Map<String, Any>{
        val principal = SecurityContextHolder.getContext().authentication.principal as Jwt
        return principal.claims
    }

    @GetMapping("/")
    fun getRoot(): Authentication{
        val user = SecurityContextHolder.getContext().authentication
        return user
    }

    @GetMapping("/social")
    fun getSocialInfo(): String {
        return "Yeet"
    }

    @GetMapping("/api/users")
    fun getAllUsers(): MutableList<User> {
        return repo.users
    }

    @GetMapping("/api/thingy")
    fun increaseThingyy(): User {
        val user = SecurityContextHolder.getContext().authentication.details as User
        user.thingy+=1
        return user
    }

}