package at.obyoxar.jwtverify

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtverifyApplicationTests {

    @Autowired
    lateinit var webClient: WebTestClient

    @Autowired
    lateinit var restTemplateTest: TestRestTemplate

    @Test
    fun rootAllowed() {
        webClient.get().uri("/").exchange().expectStatus().isOk
    }

    @Test
    fun getProtectedResource_notAllowed() {
        webClient.get().uri("/api/permissioncheck/roletester").exchange().expectStatus().isForbidden
    }

    @Test
    fun getProtectedResource_localLoginOK() {
        webClient.get()
                .uri("/auth/local?username=john&password=123")
                .exchange().also { println(it.returnResult<String>().responseHeaders) }
                .expectStatus().isOk
                .expectHeader()
                .valueMatches("Authorization", "^Bearer ([a-zA-Z0-9]+\\.){2}[a-zA-Z0-9_-]+\$")
    }

    @Test
    fun getProtectedResource_localLoginWrongCredentials() {
        webClient.get()
                .uri("/auth/local?username=john&password=1234")
                .exchange().also { println(it.returnResult<String>().responseHeaders) }
                .expectStatus().isForbidden
                .expectHeader()
                .doesNotExist("Authorization")
    }

}
