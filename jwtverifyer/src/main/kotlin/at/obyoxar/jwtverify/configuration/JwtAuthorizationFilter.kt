package at.obyoxar.jwtverify.configuration

import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder


class JwtAuthorizationFilter(authenticationManager: AuthenticationManager, var userService: UserService): BasicAuthenticationFilter(authenticationManager) {


    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authentication = getAuthentication(request)
        if(authentication != null){
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(request, response)

//        if(authentication == null){
//           chain.doFilter(request, response)
//        }
//
//        SecurityContextHolder.getContext().authentication = authentication
//        chain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = request.getHeader("Authorization")
//        val token = request.cookies?.firstOrNull { it.name == "AUTH-TOKEN" }?.value
        if(token == null){
            println("JwtAuthorization: Request didn't have a token")
            return null
        }

        if (token.isNotEmpty()) {
            try {
                val signingKey = "yeeeet".toByteArray()

                val parsedToken = Jwts.parser()
                        .setSigningKey(signingKey)
                        .parseClaimsJws(token.replace("Bearer ", ""))

                val userId = parsedToken.body.subject

                val authorities = (parsedToken.body["rol"] as List<*>).map {
                    authority -> SimpleGrantedAuthority(authority as String)
                }

                if (userId.isNotEmpty()) {
                    return UserAuthentication(userService.loadUserByUserId(userId))
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

        println("JwtAuthorization: Request didn't have a valid token: ${token}")
        return null
    }
}