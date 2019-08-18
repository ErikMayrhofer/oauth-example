package at.obyoxar.jwtverify.lib

import org.springframework.security.config.annotation.ObjectPostProcessor
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.social.security.SocialAuthenticationFilter
import org.springframework.social.security.SpringSocialConfigurer

class SuccessHandlerPostProcessor(val successHandler: AuthenticationSuccessHandler): ObjectPostProcessor<SocialAuthenticationFilter>{
    override fun <O : SocialAuthenticationFilter?> postProcess(saf: O): O {
        saf!!.setAuthenticationSuccessHandler(successHandler)
        return saf
    }


}

class OSocialConfigurer: SpringSocialConfigurer() {
    fun setSuccessHandler(successHandler: AuthenticationSuccessHandler): OSocialConfigurer{
        this.addObjectPostProcessor(SuccessHandlerPostProcessor(successHandler))
        return this
    }
}