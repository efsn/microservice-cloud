dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-hystrix")

    implementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner") {
        exclude(group = "org.spring.boot", module = "spring-boot-starter-web")
    }
}