package at.obyoxar.jwtverify.configuration

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority

class UserAuthority(
        private var authority: String,
        @JsonIgnore var user: User
): GrantedAuthority {
    override fun getAuthority(): String = authority
    fun setAuthority(auth: String){
        authority = auth
    }
}