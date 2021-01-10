package cn.elmi.microservice.producer.representation

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingAPI {
    @RequestMapping("/hi")
    fun say(@RequestParam(required = false, defaultValue = "hello") word: String) = "$word world"
}