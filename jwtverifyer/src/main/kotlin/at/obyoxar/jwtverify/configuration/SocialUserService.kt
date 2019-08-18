package at.obyoxar.jwtverify.configuration

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.social.connect.ConnectionKey
import org.springframework.social.security.SocialUserDetails
import org.springframework.social.security.SocialUserDetailsService

interface SocialUserService: SocialUserDetailsService, UserDetailsService {
    override fun loadUserByUserId(userId: String?): User
    override fun loadUserByUsername(username: String?): User
    fun loadUserByConnectionKey(connectionKey: ConnectionKey): User
    fun updateUserDetails(user: User)

}