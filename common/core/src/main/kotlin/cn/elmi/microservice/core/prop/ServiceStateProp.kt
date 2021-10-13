package cn.elmi.microservice.core.prop

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("service.state")
class ServiceStateProp {
    var env: String = "dev"

    // TODO remove by open feign
    var webClientConnectTimeout: Int = 10
    var circuitBreakerEnable: Boolean = true
}