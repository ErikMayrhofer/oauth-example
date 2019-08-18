package at.obyoxar.jwtverify.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
class StatelessAuthenticationFilter: GenericFilterBean() {

    @Autowired
    lateinit var tokenAuthenticationService: TokenAuthenticationService

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        setAuthenticationFromHeader(request as HttpServletRequest)

        chain!!.doFilter(request, response)
    }

    fun setAuthenticationFromHeader(request: HttpServletRequest){
        val authentication = SecurityContextHolder.getContext().authentication
        if(authentication !is UserAuthentication) {
            val userAuthentication = tokenAuthenticationService.getAuthentication(request)
            if (userAuthentication != null) {
                SecurityContextHolder.getContext().authentication = userAuthentication
            }
        }
    }
}
