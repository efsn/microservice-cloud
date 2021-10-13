dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-jose")

//    implementation("org.springframework.boot:spring-boot-starter-integration")
//    implementation("org.springframework.integration:spring-integration-redis")
//    implementation("org.springframework.boot:spring-boot-starter-data-redis")

//    implementation("org.keycloak:keycloak-spring-boot-starter")

    testImplementation("org.springframework.security:spring-security-test")
}
tasks {
    bootJar {
        launchScript()
    }
}
