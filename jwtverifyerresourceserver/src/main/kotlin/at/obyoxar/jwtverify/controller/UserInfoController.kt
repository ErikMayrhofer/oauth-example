package at.obyoxar.jwtverify.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserInfoController {

    @GetMapping("/free")
    @PreAuthorize("isAuthenticated()")
    fun getInfoFree(): Map<String, Any>{
        val principal = SecurityContextHolder.getContext().authentication.principal as Jwt
        return principal.claims
    }

    @GetMapping("/basic")
    @PreAuthorize("hasAnyRole('USER')")
    fun getInfoUser(): Map<String, Any>{
        val principal = SecurityContextHolder.getContext().authentication.principal as Jwt
        return principal.claims
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    fun getInfo(): Map<String, Any>{
        val principal = SecurityContextHolder.getContext().authentication.principal as Jwt
        return principal.claims
    }

    @GetMapping("/user2")
    fun getInfo2(): Map<String, Any>{
        val principal = SecurityContextHolder.getContext().authentication.principal as Jwt
        return principal.claims
    }

    @GetMapping("/")
    fun getRoot(): String{
        return "Ruut"
    }
}