package at.obyoxar.jwtverify

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources


@SpringBootApplication
@PropertySources(
        PropertySource("classpath:secrets.properties")
)
class JwtverifyApplication

fun main(args: Array<String>) {
    runApplication<JwtverifyApplication>(*args)
}

