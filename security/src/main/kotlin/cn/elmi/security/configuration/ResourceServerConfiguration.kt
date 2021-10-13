package cn.elmi.security.configuration

import cn.elmi.security.filter.AuthenticationTracingFilter
import cn.elmi.security.whitelist.WhitelistedEndpointResolver
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManagerResolver
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.access.ExceptionTranslationFilter
import javax.servlet.http.HttpServletRequest

class ResourceServerConfiguration(
    private val deniedHandler: AccessDeniedHandler,
    private val authEntryPoint: AuthenticationEntryPoint,
    private val securityProp: SecurityProp,
    private val whitelistedEndpointsResolver: WhitelistedEndpointResolver,
    private val authenticationManager: AuthenticationManagerResolver<HttpServletRequest>
) : WebSecurityConfigurerAdapter() {
    private val logger = LoggerFactory.getLogger(ResourceServerConfiguration::class.java)

    override fun configure(http: HttpSecurity) {
        logger.info("Loading security rules")
        http {
            csrf { disable() }
            addFilterBefore(AuthenticationTracingFilter(), ExceptionTranslationFilter::class.java)
            authorizeRequests {
                if (securityProp.enabled) {
                    whitelistedEndpointsResolver.resolve().forEach { ep ->
                        logger.debug("Whitelisted endpoint [${ep.uri}] with HTTP methods ${ep.methodSet.joinToString(",") { it.name }}")
                        ep.methodSet.forEach {
                            logger.info("$it ${ep.uri}")
                            authorize(it, ep.uri, permitAll)
                        }
                    }

                    authorize(HttpMethod.GET, "/**/actuator*/**", permitAll)
                    authorize(HttpMethod.HEAD, "/**/actuator*/**", permitAll)
                    authorize(HttpMethod.GET, "/**/api-docs*/**", permitAll)
                    authorize(HttpMethod.GET, "/**/docs*/**", permitAll)

                    authorize("/**")
                } else {
                    authorize("/**", permitAll)
                }
            }

            if (securityProp.enabled) {
                oauth2ResourceServer {
                    authenticationManagerResolver = authenticationManager
                    accessDeniedHandler = deniedHandler
                    authenticationEntryPoint = authEntryPoint
                }
            }
        }

        if (!securityProp.enabled) {
            http.oauth2ResourceServer().disable()
            http.oauth2Client().disable()
        }
    }


}