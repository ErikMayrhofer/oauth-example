package at.obyoxar.jwtverify.configuration

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.social.connect.ConnectionKey
import org.springframework.social.security.SocialUserDetails
import org.springframework.social.security.SocialUserDetailsService
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.authentication.AccountStatusUserDetailsChecker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.social.connect.Connection


@Service
class UserService: SocialUserService {
    override fun findUserIdsConnectedTo(providerId: String, providerUserIds: MutableSet<String>) = userRepo.findUserIdsConnectedTo(providerId, providerUserIds)
    override fun findUserIdsWithConnection(connection: Connection<*>) = userRepo.findUserIdsWithConnection(connection)

    @Autowired
    private lateinit var userRepo: UserRepository

    private val detailsChecker = AccountStatusUserDetailsChecker()

//    @Transactional(readOnly = true)
    override fun loadUserByUserId(userId: String?): User {
        val user = userRepo.findById(java.lang.Long.valueOf(userId!!))
        return checkUser(user)
    }

//    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String?): User {
        val user = userRepo.findByUsername(username!!)
        return checkUser(user)
    }

//    @Transactional(readOnly = true)
//    override fun loadUserByConnectionKey(connectionKey: ConnectionKey): User {
//        val user = userRepo.findByProviderIdAndProviderUserId(connectionKey.providerId, connectionKey.providerUserId)
//        return checkUser(user)
//    }

    override fun updateUserDetails(user: User) {
        userRepo.save(user)
    }

    private fun checkUser(user: User?): User {
        if (user == null) {
            throw UsernameNotFoundException("user not found")
        }
        detailsChecker.check(user)
        return user
    }
}