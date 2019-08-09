package at.obyoxar.jwtverify.lib

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.RemoteKeySourceException
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.jwk.source.RemoteJWKSet
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jose.util.Resource
import com.nimbusds.jose.util.ResourceRetriever
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import org.springframework.core.convert.converter.Converter
import org.springframework.http.*
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms
import org.springframework.security.oauth2.jwt.*
import org.springframework.util.Assert
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.text.ParseException
import java.time.Instant
import java.util.LinkedHashMap

private val DECODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to decode the Jwt: %s"

class OJwtDecoderBase constructor(jwkSource: JWKSource<SecurityContext>, jwsAlgorithm: String) : JwtDecoder {

    private val jwsAlgorithm: JWSAlgorithm = JWSAlgorithm.parse(jwsAlgorithm)
    private val jwtProcessor: ConfigurableJWTProcessor<SecurityContext> = DefaultJWTProcessor()

    var claimSetConverter: Converter<Map<String, Any>, Map<String, Any>> = MappedJwtClaimSetConverter.withDefaults(emptyMap())
    var jwtValidator = JwtValidators.createDefault()

    init {
        val jwsKeySelector = JWSVerificationKeySelector(this.jwsAlgorithm, jwkSource)
        this.jwtProcessor.jwsKeySelector = jwsKeySelector
        this.jwtProcessor.setJWTClaimsSetVerifier { claims, context -> }
    }

    override fun decode(token: String): Jwt {
        val jwt = this.parse(token)
        if (jwt is SignedJWT) {
            val createdJwt = this.createJwt(token, jwt)
            return this.validateJwt(createdJwt)
        }
        throw JwtException("Unsupported algorithm of " + jwt.header.algorithm)
    }

    private fun parse(token: String): JWT {
        return JWTParser.parse(token)
    }

    private fun createJwt(token: String, parsedJwt: JWT): Jwt {
        val jwtClaimsSet = this.jwtProcessor.process(parsedJwt, null)

        val headers = LinkedHashMap(parsedJwt.header.toJSONObject())
        val claims = this.claimSetConverter.convert(jwtClaimsSet.claims)

        val expiresAt = claims!![JwtClaimNames.EXP] as Instant
        val issuedAt = claims[JwtClaimNames.IAT] as Instant
        return Jwt(token, issuedAt, expiresAt, headers, claims)
    }

    private fun validateJwt(jwt: Jwt): Jwt {
        val result = this.jwtValidator.validate(jwt)
        if (result.hasErrors()) {
            val description = result.errors.iterator().next().description
            throw JwtValidationException(
                    String.format(DECODING_ERROR_MESSAGE_TEMPLATE, description),
                    result.errors)
        }
        return jwt
    }
}