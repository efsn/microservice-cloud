package cn.elmi.microservice.consumer.representation

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient
import org.springframework.cloud.client.serviceregistry.Registration
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class GreetingAPI(
    private val loadBalancer: LoadBalancerClient,
    private val discoveryClient: DiscoveryClient,
    private val registration: Registration,
    private val env: Environment,
    private val sampleClient: SampleClient,
    private val restTemplate: RestTemplate
) {

    @Value("\${spring.application.name:xx}")
    private lateinit var appName: String

    @Value("\${external.hello:xx}")
    private lateinit var helloAppName: String

    @RequestMapping("me")
    fun me() = registration

    @RequestMapping("rest")
    fun rest() = restTemplate.getForObject("http://$helloAppName/hi", String::class.java)

    @RequestMapping("choose")
    fun choose() = loadBalancer.choose(helloAppName).uri.toString()

    @RequestMapping("env")
    fun env(@RequestParam prop: String) = env.getProperty(prop, "not found")

    @RequestMapping("instances")
    fun instances() = discoveryClient.getInstances(appName)

    @RequestMapping("feign/{word}")
    fun feign(@RequestParam word: String) = sampleClient.choose(word)
}

@Configuration
class Configure {
    @Bean
    @LoadBalanced
    fun restTemplate() = RestTemplate()
}

@FeignClient("consul-producer-app")
interface SampleClient {
    @RequestMapping("/hi/{word}", method = [RequestMethod.GET])
    fun choose(@RequestParam word: String): String
}