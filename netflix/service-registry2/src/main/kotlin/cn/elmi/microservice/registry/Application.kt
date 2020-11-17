package cn.elmi.microservice.registry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@EnableEurekaClient
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@RestController
class HelloAPI(
    private val client: DiscoveryClient
) {

    @RequestMapping("hello")
    fun say() = client.getInstances("hello-service").size

}