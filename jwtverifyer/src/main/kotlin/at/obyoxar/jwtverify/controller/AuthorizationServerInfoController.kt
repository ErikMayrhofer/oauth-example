package at.obyoxar.jwtverify.controller

import at.obyoxar.jwtverify.meta.AuthorizationServerInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class AuthorizationServerInfoController {

    @Autowired
    lateinit var authorizationServerInfo: AuthorizationServerInfo

    @GetMapping
    fun root(): AuthorizationServerInfo{
        return authorizationServerInfo.decorateWithLoginData()
    }
}