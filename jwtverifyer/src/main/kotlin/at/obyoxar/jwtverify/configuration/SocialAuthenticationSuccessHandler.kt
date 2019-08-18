package at.obyoxar.jwtverify.configuration

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SocialAuthenticationSuccessHandler: SavedRequestAwareAuthenticationSuccessHandler() {

    @Autowired
    lateinit var userService: SocialUserService

    @Autowired
    lateinit var tokenAuthenticationService: TokenAuthenticationService

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        println("AuthenticationSuccess: ${authentication!!.name}")
        val authenticatedUser = userService.loadUserByUsername(authentication.name);
        println(" User: ${authenticatedUser}")

        // Add UserAuthentication to the response
//        val userAuthentication = UserAuthentication(authenticatedUser);
//        tokenAuthenticationService.addAuthentication(response!!, userAuthentication);

        val user = authentication.principal as User
        val roles = user.authorities.map { it.authority }

        val token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, "yeeeet".toByteArray())
                .setHeaderParam("typ", "JWT")
                .setIssuer("mySampleIssuer")
                .setAudience("mySampleAudience")
                .setSubject(user.username)
                .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .claim("rol", roles)
                .claim("from", "sociallogin")
                .compact()

        response.addHeader("Authorization", "Bearer $token")
        val cookie = Cookie("AUTH-TOKEN", token)
        cookie.path = "/"
        response.addCookie(cookie)

        super.onAuthenticationSuccess(request, response, authentication);
    }
}