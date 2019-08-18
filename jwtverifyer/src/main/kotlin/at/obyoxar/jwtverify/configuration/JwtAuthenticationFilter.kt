package at.obyoxar.jwtverify.configuration

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(authenticationManager: AuthenticationManager): UsernamePasswordAuthenticationFilter() {

    init {
        setFilterProcessesUrl("/auth/local")

        this.authenticationManager = authenticationManager
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication {
        val username = request.getParameter("username")
        val password = request.getParameter("password")
        val token = UsernamePasswordAuthenticationToken(username, password)

        return authenticationManager.authenticate(token)
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        val user = authResult.principal as User
        println("JwtAuthentication successfulAuthentication: $user")
        val roles = user.authorities.map { it.authority }

        val token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, "yeeeet".toByteArray())
                .setHeaderParam("typ", "JWT")
                .setIssuer("mySampleIssuer")
                .setAudience("mySampleAudience")
                .setSubject(user.userId)
                .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .claim("rol", roles)
                .compact()

        response.addHeader("Authorization", "Bearer $token")
        val cookie = Cookie("AUTH-TOKEN", token)
        cookie.path = "/"
        response.addCookie(cookie)
    }
}