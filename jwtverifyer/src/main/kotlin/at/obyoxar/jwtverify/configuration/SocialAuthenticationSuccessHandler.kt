package at.obyoxar.jwtverify.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SocialAuthenticationSuccessHandler: SavedRequestAwareAuthenticationSuccessHandler() {

    @Autowired
    lateinit var userService: SocialUserService

    @Autowired
    lateinit var tokenAuthenticationService: TokenAuthenticationService

    override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        println("AuthenticationSuccess: ${authentication!!.name}")
        val authenticatedUser = userService.loadUserByUsername(authentication.name);

        // Add UserAuthentication to the response
        val userAuthentication = UserAuthentication(authenticatedUser);
        tokenAuthenticationService.addAuthentication(response!!, userAuthentication);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}