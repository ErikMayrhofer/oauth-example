package at.obyoxar.jwtverify.configuration

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

@Component
class DebugAuthFilter: GenericFilterBean() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val auth = SecurityContextHolder.getContext().authentication
        println(auth)
        chain!!.doFilter(request, response)
    }
}