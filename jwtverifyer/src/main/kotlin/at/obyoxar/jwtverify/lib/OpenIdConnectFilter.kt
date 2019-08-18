package at.obyoxar.jwtverify.lib

import com.auth0.jwk.UrlJwkProvider
import org.codehaus.jackson.map.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.jwt.JwtHelper
import org.springframework.security.jwt.crypto.sign.RsaVerifier
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import java.lang.UnsupportedOperationException
import java.net.URL
import java.security.interfaces.RSAPublicKey
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import java.util.*

/*
GOOGLE OAUTH EXAMPLE FLOW: First:
https://accounts.google.com/o/oauth2/auth?client_id=886052977857-jo6b9jn36ks4phf1005nt4bdpcnu5fb0.apps.googleusercontent.com&response_type=code&scope=openid%20email&redirect_uri=http://localhost:8080/google-login
https://accounts.google.com/o/oauth2/auth?client_id=886052977857-jo6b9jn36ks4phf1005nt4bdpcnu5fb0.apps.googleusercontent.com&response_type=code&scope=openid%20email&redirect_uri=http://localhost:8080/google-login&state=abc



http://localhost:8080/google-login?state=abc&code=4%2FpAEAhEbaMKC1tAqERFMMxcJB0E7kHzu67O66A27paWCqBgXgheg-ZrPrcO1LZbDMW7vw2Y7I_wTouco6McDPySQ&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&session_state=decba0e6051fd6c351ef6084dc930e6e5f14398c..5d35&prompt=consent

 */









inline fun <reified A, reified B> Pair<*, *>.asPairOf(): Pair<A, B> {
//    if (first !is A || second !is B)
    return first as A to second as B
}

inline fun <reified R, reified T> Map<*, *>.asMapOf() = this.map {
    (it.key to it.value).asPairOf<R, T>()
}.toMap()

class OpenIdConnectFilter(
        defaultFilterProcessUrl: String,
        val restTemplate: OAuth2RestTemplate
        ): AbstractAuthenticationProcessingFilter(defaultFilterProcessUrl) {
    @Value("\${google.clientId}")
    private lateinit var clientId: String

    @Value("\${google.issuer}")
    private lateinit var issuer: String

    @Value("\${google.jwkUrl}")
    private lateinit var jwkUrl: String

    init {
        setAuthenticationManager {
            throw UnsupportedOperationException("No authentication should be done with OpenIdConnectFilter")
        }
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val accessToken = restTemplate.accessToken
        val idToken = accessToken.additionalInformation["id_token"].toString()
        val kid = JwtHelper.headers(idToken)["kid"]!!
        val tokenDecoded = JwtHelper.decodeAndVerify(idToken, verifier(kid))
        val authInfo = ObjectMapper().readValue(tokenDecoded.claims, Map::class.java).asMapOf<String, String>()
        verifyClaims(authInfo)
        val user = OpenIdConnectUserDetails(authInfo, accessToken)
        return UsernamePasswordAuthenticationToken(user, null, user.authorities)

    }

    fun verifyClaims(claims: Map<*, *>) {
        val exp = claims["exp"] as Int
        val expireDate = Date(exp * 1000L)
        val now = Date()
        if (expireDate.before(now) || claims["iss"] != issuer || claims["aud"] != clientId) {
            throw RuntimeException("Invalid claims")
        }
    }

    private fun verifier(kid: String): RsaVerifier{
        val provider = UrlJwkProvider(URL(jwkUrl))
        val jwk = provider.get(kid)
        return RsaVerifier(jwk.publicKey as RSAPublicKey)
    }
}