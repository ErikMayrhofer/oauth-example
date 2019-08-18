package at.obyoxar.jwtverify.lib

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.common.OAuth2AccessToken

class OpenIdConnectUserDetails(
        val userId: String,
        username: String,
        val token: OAuth2AccessToken
): UserDetails {

    val _username: String = username

    override fun getUsername(): String {
        return _username
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("ROLE_USER"), SimpleGrantedAuthority("ROLE_USER_OPENID"))
    }

    override fun isEnabled(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String? = null

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    constructor(userInfo: Map<String, String>, token: OAuth2AccessToken):
            this(
                    userInfo["sub"] ?: error("Userinfo must contain a 'sub' field"),
                    userInfo["email"] ?: error("UserInfo must contain a 'email' field"),
                    token
            )
}