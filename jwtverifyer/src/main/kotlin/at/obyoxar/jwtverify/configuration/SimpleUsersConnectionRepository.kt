package at.obyoxar.jwtverify.configuration
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.social.connect.Connection
//import org.springframework.social.connect.ConnectionRepository
//import org.springframework.social.connect.UsersConnectionRepository
//import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository
//import org.springframework.stereotype.Service
//
////class SimpleUsersConnectionRepository: UsersConnectionRepository {
//class SimpleUsersConnectionRepository: InMemoryUsersConnectionRepository() {
//
//    lateinit var userService: SocialUserService
//
//    override fun findUserIdsConnectedTo(providerId: String?, providerUserIds: MutableSet<String>?): MutableSet<String> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun findUserIdsWithConnection(connection: Connection<*>?): MutableList<String> {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun createConnectionRepository(userId: String?): ConnectionRepository {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//}