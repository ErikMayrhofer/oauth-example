package at.obyoxar.jwtverify.configuration

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class UserAuthentication(val user: User): Authentication {

    private var _authenticated = true

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = user.authorities

    override fun setAuthenticated(isAuthenticated: Boolean) {
        _authenticated = isAuthenticated
    }

    override fun getName(): String = user.username

    override fun getCredentials(): Any? = null

    override fun getPrincipal(): Any = user

    override fun isAuthenticated(): Boolean = _authenticated

    override fun getDetails(): Any = user
}