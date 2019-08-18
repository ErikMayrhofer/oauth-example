package at.obyoxar.jwtverify.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.xml.bind.DatatypeConverter


private val AUTH_HEADER_NAME = "X-AUTH-TOKEN"
private val AUTH_COOKIE_NAME = "AUTH-TOKEN"
private val TEN_DAYS = (1000 * 60 * 60 * 24 * 10).toLong()

@Service
class TokenAuthenticationService {
    @Value("\${token.secret}")
    final var secret: String = "abcabc"

    val tokenHandler: TokenHandler = TokenHandler(DatatypeConverter.parseBase64Binary(secret))

    fun addAuthentication(response: HttpServletResponse, authentication: UserAuthentication) {
        val user = authentication.details as User
        user.expires = (System.currentTimeMillis() + TEN_DAYS)
        val token = tokenHandler.createTokenForUser(user)

        // Put the token into a cookie because the client can't capture response
        // headers of redirects / full page reloads.
        // (Its reloaded as a result of this response triggering a redirect back to "/")
        response.addCookie(createCookieForToken(token))
    }

    fun getAuthentication(request: HttpServletRequest): UserAuthentication? {
        // to prevent CSRF attacks we still only allow authentication using a custom HTTP header
        // (it is up to the client to read our previously set cookie and put it in the header)
        val token = request.getHeader(AUTH_HEADER_NAME)
        if (token != null) {
            val user = tokenHandler.parseUserFromToken(token)
            if (user != null) {
                return UserAuthentication(user)
            }
        }
        return null
    }

    private fun createCookieForToken(token: String): Cookie {
        val authCookie = Cookie(AUTH_COOKIE_NAME, token)
        authCookie.path = "/"
        return authCookie
    }
}
