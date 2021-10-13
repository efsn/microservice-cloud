dependencies {
    // kotlin coroutine
//    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
//    api("org.jetbrains.kotlinx:kotlinx-coroutines-core")
//    api("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    // TODO version compatibility
    api("org.springframework.kotlin:spring-kotlin-coroutine:0.3.7") {
        exclude("org.jetbrains.kotlinx")
    }

    // spring data
    api("org.springframework:spring-tx")
    api("org.springframework.boot:spring-boot-starter-jdbc")
    api("org.springframework.boot:spring-boot-starter-data-redis-reactive")
//    api("org.springframework.boot.experimental:spring-boot-starter-data-r2dbc")
    api("org.flywaydb:flyway-core")

    // spring security
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.security:spring-security-oauth2-resource-server")
    api("org.springframework.security:spring-security-oauth2-client")
    api("org.springframework.security:spring-security-oauth2-jose")

    // jwt
    api("io.jsonwebtoken:jjwt-api")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson")

    // reactor
    api("io.projectreactor.addons:reactor-extra")
    api("io.projectreactor.kotlin:reactor-kotlin-extensions")

    // open feign
    api("com.playtika.reactivefeign:feign-reactor-spring-cloud-starter:2.0.26")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

//    api("org.springframework.cloud:spring-cloud-starter-gateway")
//    api("org.springframework.cloud:spring-cloud-starter-netflix-hystrix")
//
//    api("org.springframework.cloud:spring-cloud-starter-contract-stub-runner") {
//        exclude(group = "org.spring.boot", module = "spring-boot-starter-web")
//    }
}