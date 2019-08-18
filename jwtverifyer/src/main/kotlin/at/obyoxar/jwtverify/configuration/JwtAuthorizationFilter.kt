package at.obyoxar.jwtverify.configuration

import io.jsonwebtoken.*
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder


class JwtAuthorizationFilter(authenticationManager: AuthenticationManager): BasicAuthenticationFilter(authenticationManager) {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authentication = getAuthentication(request)
        if(authentication == null){
           chain.doFilter(request, response)
        }

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val tokenA = request.getHeader("Authorization ")
        val tokenB = request.cookies.first { it.name == "AUTH-TOKEN" }.value
        val token = tokenA ?: "Bearer $tokenB"
        if (token.isNotEmpty() && token.startsWith("Bearer ")) {
            try {
                val signingKey = "yeeeet".toByteArray()

                val parsedToken = Jwts.parser()
                        .setSigningKey(signingKey)
                        .parseClaimsJws(token.replace("Bearer ", ""))

                val username = parsedToken.body.subject

                val authorities = (parsedToken.body["rol"] as List<*>).map {
                    authority -> SimpleGrantedAuthority(authority as String)
                }

                if (username.isNotEmpty()) {
                    return UsernamePasswordAuthenticationToken(username, null, authorities)
                }
            } catch (exception: ExpiredJwtException) {
                println("Request to parse expired JWT : ${token} failed : ${exception.message}")
            } catch (exception: UnsupportedJwtException) {
                println("Request to parse unsupported JWT : ${token} failed : ${exception.message}")
            } catch (exception: MalformedJwtException) {
                println("Request to parse invalid JWT : ${token} failed : ${exception.message}")
            } catch (exception: SignatureException) {
                println("Request to parse JWT with invalid signature : ${token} failed : ${exception.message}")
            } catch (exception: IllegalArgumentException) {
                println("Request to parse empty or null JWT : ${token} failed : ${exception.message}")
            }

        }

        return null
    }
}