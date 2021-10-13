package cn.elmi.microservice.core.configuration

import cn.elmi.microservice.core.prop.SecurityProp
import com.nimbusds.jose.jwk.RSAKey
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableConfigurationProperties(SecurityProp::class)
class SecurityConfiguration(private val securityProp: SecurityProp) {

    @Bean
    fun rsaPublicKey(): RSAKey = RSAKey.parse(securityProp.rsaPublicKey)

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity, rsaKey: RSAKey): SecurityWebFilterChain {
        http.csrf().disable()
            .httpBasic().and()
            .authorizeExchange()
            .pathMatchers("/v2/api-docs").hasRole("SWAGGER_ADMIN")
            .anyExchange().permitAll()
            .and().formLogin()
            .and().oauth2ResourceServer().jwt().publicKey(rsaKey.toRSAPublicKey())
        return http.build()
    }

}