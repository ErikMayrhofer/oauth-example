package at.obyoxar.jwtverify.configuration

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.springframework.security.core.GrantedAuthority

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class, property = "@id")
class UserAuthority(
        private var authority: String,
        var user: User
): GrantedAuthority {
    override fun getAuthority(): String = authority
    fun setAuthority(auth: String){
        authority = auth
    }
}