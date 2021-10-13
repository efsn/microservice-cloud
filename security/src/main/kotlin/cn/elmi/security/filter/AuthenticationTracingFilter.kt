package cn.elmi.security.filter

import org.slf4j.MDC
import org.springframework.security.core.context.SecurityContextHolder
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class AuthenticationTracingFilter : Filter {

    private val USERNAME = "USERNAME"

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        SecurityContextHolder.getContext().authentication?.apply { MDC.put(USERNAME, name) }
        try {
            chain?.doFilter(request, response)
        } finally {
            MDC.remove(USERNAME)
        }
    }

}