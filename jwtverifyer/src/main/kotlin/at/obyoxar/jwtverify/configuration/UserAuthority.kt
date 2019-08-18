package at.obyoxar.jwtverify.configuration

import org.springframework.security.core.GrantedAuthority

class UserAuthority(
        private var authority: String,
        var user: User
): GrantedAuthority {
    override fun getAuthority(): String = authority
    fun setAuthority(auth: String){
        authority = auth
    }
}