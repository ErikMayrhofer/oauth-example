import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.1.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
    kotlin("jvm") version "1.2.71"
    kotlin("plugin.spring") version "1.2.71"
    kotlin("kapt") version "1.2.71"
}

group = "at.obyoxar"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-data-rest")

    implementation("com.google.api-client:google-api-client:1.30.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.security:spring-security-oauth2-jose:5.1.6.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.1.7.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-security:2.1.7.RELEASE")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:2.1.7.RELEASE")
    implementation("org.springframework.security.oauth:spring-security-oauth2:2.3.6.RELEASE")
    implementation("org.springframework.security:spring-security-jwt:1.0.10.RELEASE")

    implementation("org.springframework.boot:spring-boot-starter-actuator:2.1.6.RELEASE")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("com.sun.xml.bind:jaxb-core:2.3.0.1")
    implementation("com.sun.xml.bind:jaxb-impl:2.3.0.1")
    implementation("javax.activation:activation:1.1.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}