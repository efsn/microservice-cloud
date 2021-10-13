package cn.elmi.security.whitelist

import org.springframework.http.HttpMethod
import java.util.*

class WhitelistedEndpoint(val uri: String, val methodSet: Set<HttpMethod> = emptySet()) {

    fun addMethod(vararg method: HttpMethod) = methodSet + setOf(method)

    override fun equals(other: Any?) = when {
        this === other -> true
        other == null || javaClass != other.javaClass -> false
        Objects.equals(uri, (other as WhitelistedEndpoint).uri) && (methodSet + other.methodSet).size == methodSet.size -> true
        else -> false
    }

    override fun hashCode() = Objects.hash(uri, methodSet)

}