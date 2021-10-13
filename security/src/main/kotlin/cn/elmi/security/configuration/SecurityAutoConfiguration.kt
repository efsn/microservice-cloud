package cn.elmi.security.configuration

import cn.elmi.security.authentication.AuthenticationService
import cn.elmi.security.error.ForbiddenHandler
import cn.elmi.security.error.UnauthorizedEntryPoint
import cn.elmi.security.whitelist.WhitelistedEndpointResolver
import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jwt.JWTParser
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationManagerResolver
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.HttpServletRequest

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(prefix = "security", name = ["enabled"], havingValue = "true", matchIfMissing = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("cn.elmi.security")
@EnableConfigurationProperties(SecurityProp::class)
class SecurityAutoConfiguration(private val securityProp: SecurityProp) {
    private val cache = ConcurrentHashMap<String, AuthenticationManager>()

    @Bean
    @ConditionalOnMissingBean(AuthenticationService::class)
    fun authenticationService() = AuthenticationService(securityProp)

    @Bean
    @ConditionalOnMissingBean(BearerTokenResolver::class)
    fun bearerTokenResolver() = DefaultBearerTokenResolver()

    @Bean
    fun whitelistedEndpointResolver() = WhitelistedEndpointResolver(securityProp)

    @Bean
    fun authorizedClientManager(clientRegistrationRepository: ClientRegistrationRepository, clientService: OAuth2AuthorizedClientService) =
        AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService)
            .apply {
                setAuthorizedClientProvider(OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build())
            }

    @Bean
    @ConditionalOnMissingBean(AuthenticationManagerResolver::class)
    fun authenticationManager(bearerTokenResolver: BearerTokenResolver) = AuthenticationManagerResolver<HttpServletRequest> {
        val token = bearerTokenResolver.resolve(it)
        // TODO catch exception
        val issuer = JWTParser.parse(token).jwtClaimsSet.issuer

        if (!cache.containsKey(issuer)) {
            val service = securityProp.services.values.first { prop -> prop.iss == issuer }
            cache[issuer] = jwt(service)
        }

        cache[issuer] ?: throw NoSuchElementException("the authorization token issuer is missing")
    }

    @Bean
    fun accessDeniedHandler(objectMapper: ObjectMapper) = ForbiddenHandler(objectMapper)

    @Bean
    fun authenticationEntryPoint(objectMapper: ObjectMapper) = UnauthorizedEntryPoint(objectMapper)

    @Bean
    fun oAuth2ResourceServerConfiguration(
        accessDeniedHandler: AccessDeniedHandler,
        authenticationEntryPoint: AuthenticationEntryPoint,
        serviceAuthenticationManager: AuthenticationManagerResolver<HttpServletRequest>,
        whitelistedEndpointsResolver: WhitelistedEndpointResolver,
        securityProp: SecurityProp
    ) = ResourceServerConfiguration(accessDeniedHandler, authenticationEntryPoint, securityProp, whitelistedEndpointsResolver, serviceAuthenticationManager)

    private fun jwt(service: SecurityProp.ServiceProp) = AuthenticationManager {
        val jwtDecoder = when {
            service.conf.isNotBlank() -> JwtDecoders.fromIssuerLocation(service.conf)
            service.jwks.isNotBlank() -> NimbusJwtDecoder.withJwkSetUri(service.jwks).build()
            service.iss.isNotBlank() -> JwtDecoders.fromIssuerLocation(service.iss)
            else -> throw IllegalAccessException("service config error")
        }
        JwtAuthenticationProvider(jwtDecoder).apply { setJwtAuthenticationConverter(JwtBearerTokenAuthenticationConverter()) }.authenticate(it)
    }
}