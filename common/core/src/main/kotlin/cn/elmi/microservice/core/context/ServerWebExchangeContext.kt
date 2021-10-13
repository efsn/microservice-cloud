package cn.elmi.microservice.core.context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.reactor.ReactorContext
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange

val CoroutineScope.serverWebExchange: ServerWebExchange?
    get() = coroutineContext[ReactorContext]?.context?.getOrDefault<ServerWebExchange>(ServerWebExchangeContextWebFilter, null)

val CoroutineScope.request: ServerHttpRequest?
    get() = serverWebExchange?.request

val CoroutineScope.response: ServerHttpResponse?
    get() = serverWebExchange?.response

val CoroutineScope.authorization: String?
    get() = request?.headers?.get(AUTHORIZATION)?.firstOrNull()

val CoroutineScope.traceId: String
    get() = request?.headers?.get("x-trace-id")?.firstOrNull() ?: ""

val CoroutineScope.clientIp: String
    get() = (request?.headers?.get("x-forwarded-for")?.firstOrNull()
        ?: request?.headers?.get("Proxy-Client-IP")?.firstOrNull()
        ?: request?.headers?.get("WL-Proxy-Client-IP")?.firstOrNull()
        ?: request?.headers?.get("HTTP_CLIENT_IP")?.firstOrNull()
        ?: request?.headers?.get("HTTP_X_FORWARDED_FOR")?.firstOrNull()
        ?: request?.remoteAddress?.address?.hostAddress ?: "0.0.0.0").split(",")[0]