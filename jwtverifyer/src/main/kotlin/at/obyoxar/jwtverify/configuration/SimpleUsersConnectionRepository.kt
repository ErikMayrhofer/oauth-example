package at.obyoxar.jwtverify.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.social.connect.*
import org.springframework.social.connect.mem.TemporaryConnectionRepository


class SimpleUsersConnectionRepository(
        val userService: UserService,
        val connectionFactoryLocator: ConnectionFactoryLocator,
        val connectionSignUp: ConnectionSignUp
): UsersConnectionRepository {
//class SimpleUsersConnectionRepository: InMemoryUsersConnectionRepository() {


    override fun findUserIdsConnectedTo(providerId: String, providerUserIds: MutableSet<String>): MutableSet<String> = userService.findUserIdsConnectedTo(providerId, providerUserIds)

    override fun findUserIdsWithConnection(connection: Connection<*>): MutableList<String> = userService.findUserIdsWithConnection(connection)

    override fun createConnectionRepository(userId: String): ConnectionRepository {
        val repo = TemporaryConnectionRepository(connectionFactoryLocator)
        val user = userService.loadUserByUserId(userId)
        val connectionData = ConnectionData(
                user.providerId,
                user.providerUserId,
                null, null, null,
                user.accessToken,
                null, null, null
        )
        val connection = connectionFactoryLocator.getConnectionFactory(user.providerId).createConnection(connectionData)
        repo.addConnection(connection)
        return repo
    }
}

