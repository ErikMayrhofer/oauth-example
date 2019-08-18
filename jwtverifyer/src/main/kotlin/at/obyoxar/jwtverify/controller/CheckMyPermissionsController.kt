package at.obyoxar.jwtverify.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/permissioncheck")
class CheckMyPermissionsController {

    @GetMapping("free")
    fun free(): String{
        return "Ok"
    }

    @GetMapping("roleuser")
    @PreAuthorize("hasRole('USER')")
    fun roleUser(): String{
        return "Ok"
    }

    @GetMapping("roletester")
    @PreAuthorize("hasRole('TESTER')")
    fun roleTester(): String{
        return "Ok"
    }

}