dependencies {
    api("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage")
    }
    api("org.junit.jupiter:junit-jupiter-api")
    api("org.springframework.security:spring-security-test")

    runtimeOnly("org.junit.jupiter:junit-jupiter-engine")
}