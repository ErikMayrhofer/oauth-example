package at.obyoxar.jwtverify.configuration

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.social.security.SocialUserDetails

var globalUserId: Long = 0

class User(password: String? = null, userId: Long? = null, username: String? = null,
           var providerId: String = "",
           var providerUserId: String = ""
           ): SocialUserDetails {

    private var _password = password ?: ""
    private var _userName = username ?: ""
    private var _userId = userId ?: globalUserId++

    var accessToken: String = ""
    var expires: Long = 0
    val authorities = mutableListOf<UserAuthority>()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("USER"))
    }

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = _userName

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getUserId(): String = _userId.toString()
    fun getUserIdLong(): Long = _userId

    override fun getPassword(): String = _password

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    fun setUserId(userId: Long){
        _userId = userId
    }

    fun setPassword(password: String){
        _password = password
    }

    fun setUsername(username: String){
        _userName = username
    }

    fun grantRole(user: UserRole) {

    }
}