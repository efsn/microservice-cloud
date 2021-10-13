package cn.elmi.security.whitelist

import cn.elmi.security.configuration.SecurityProp
import org.springframework.http.HttpMethod

class WhitelistedEndpointResolver(private val securityProp: SecurityProp) {

    private val DEFAULT_METHOD = HttpMethod.GET

    fun resolve(): Collection<WhitelistedEndpoint> =
        if (securityProp.enabled) {
            val map = LinkedHashMap<String, WhitelistedEndpoint>()
            securityProp.whitelisted.forEach { path ->
                val index = path.lastIndexOf(":")
                val methodSet = if (index < 0) setOf(DEFAULT_METHOD) else toHttpMethod(path.substring(index + 1))
                val uri = (if (index < 0) path else path.substring(0, index)).trim()
                map.computeIfAbsent(uri) { WhitelistedEndpoint(it, methodSet) }
            }
            map.values
        } else emptySet()


    private fun toHttpMethod(method: String) =
        if (method.isBlank()) setOf(DEFAULT_METHOD)
        else method.split(",", ";").mapNotNull { HttpMethod.resolve(it.trim().toUpperCase()) }.toSet()

}