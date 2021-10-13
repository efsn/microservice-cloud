package cn.elmi.security.representation

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("demo")
class DemoAPI {

//    @PreAuthorize("authenticationService.hasAccess4Brand('*')")
//    @PreAuthorize("hasAuthority('demo:test:demo:get')")
//    @PreAuthorize("hasAuthority('demo:test:demo:get')")
    @GetMapping("/get")
    fun demoGet(): String {

        val x = SecurityContextHolder.getContext().authentication.authorities

        return "demo-get"
    }

    @PostMapping("/post")
    fun demoPost(): String {
        return "demo-post"
    }

    @PutMapping("/put")
    fun demoPut(): String {
        return "demo-put"
    }

}