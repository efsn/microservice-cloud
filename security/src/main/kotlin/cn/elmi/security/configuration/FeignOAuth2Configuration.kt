package cn.elmi.security.configuration

import feign.RequestInterceptor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.access.AuthorizationServiceException
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager

class FeignOAuth2Configuration {

    @Bean
    @ConditionalOnProperty(prefix = "security", name = ["enabled"], havingValue = "true", matchIfMissing = true)
    fun oAuth2RequestInterceptor(authorizedClientManager: OAuth2AuthorizedClientManager) = RequestInterceptor {
        it.header(AUTHORIZATION, getAccessToken(authorizedClientManager))
    }

    private fun getAccessToken(authorizedClientManager: OAuth2AuthorizedClientManager): String {
        val authentication = AnonymousAuthenticationToken("key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"))
        val authorizedRequest = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak").principal(authentication).build()
        val authorizedClient = authorizedClientManager.authorize(authorizedRequest)
            ?: throw AuthorizationServiceException("Issue while authorize the service on keycloak")
        return "Bearer ${authorizedClient.accessToken.tokenValue}"
    }
}