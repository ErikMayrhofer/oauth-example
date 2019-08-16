
package at.obyoxar.jwtverify.controller

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.web.bind.annotation.*
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException
import org.springframework.security.oauth2.provider.OAuth2Request
import java.io.Serializable

data class User(var name: String)

class MyAuth(var userName: String): Authentication{

    private var authenticated = false

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("YEETEN"))
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }

    override fun getName(): String {
        return userName
    }

    override fun getCredentials(): Any {
        return userName
    }

    override fun getPrincipal(): Any {
        return User(userName)
    }

    override fun isAuthenticated(): Boolean {
        return authenticated
    }

    override fun getDetails(): Any {
        return userName
    }

}


data class RequestParams(val idToken: String)

@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
class VerifyerController {

    companion object{
        final val CLIENT_ID = "886052977857-jo6b9jn36ks4phf1005nt4bdpcnu5fb0.apps.googleusercontent.com"
    }

    @Autowired
    lateinit var tokenStore: TokenStore

    @Autowired
    lateinit var tokenService: DefaultTokenServices


    @GetMapping  ("/verify")
    fun greeting(@RequestBody params: RequestParams): OAuth2AccessToken? {
        val transport = NetHttpTransport()
        val jsonFactory = JacksonFactory()
        val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory).setAudience(listOf(CLIENT_ID)).build()

        val idToken = verifier.verify(params.idToken)
        if (idToken != null) {
            val payload = idToken.payload

            val userId = payload.subject
            println("User ID: $userId")

            val email = payload.email
            val emailVerified = java.lang.Boolean.valueOf(payload.emailVerified)
            val name = payload["name"] as String
            val pictureUrl = payload["picture"] as String
            val locale = payload["locale"] as String
            val familyName = payload["family_name"] as String
            val givenName = payload["given_name"] as String
            val uid = payload.subject


            val authorities = hashSetOf(SimpleGrantedAuthority("ROLE_USER"))
            val requestParameters = hashMapOf<String, String>()
            val clientId = "acme"
            val approved = true
            val scope = hashSetOf("scope")
            val resourceIds = hashSetOf<String>()
            val responseTypes = hashSetOf("code")
            val extensionProperties = hashMapOf<String, Serializable>()

            val request = OAuth2Request(
                    requestParameters,
                    clientId,
                    authorities,
                    approved,
                    scope,
                    resourceIds,
                    null,
                    responseTypes,
                    extensionProperties
            )

            val accessToken = tokenService.createAccessToken(OAuth2Authentication(request, MyAuth(name)))

            println(accessToken)

            return accessToken

        } else {
            println("Invalid ID token.")
        }
        throw UserDeniedAuthorizationException("Invalid ID token.")
    }
}