package cn.elmi.microservice.core.context

import cn.elmi.microservice.core.prop.ServiceStateProp
import cn.elmi.microservice.core.extension.firstOrNull
import org.springframework.stereotype.Component
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class ServerWebExchangeContextWebFilter(
    private val requestMappingHandlerMapping: RequestMappingHandlerMapping,
    private val serviceStateProp: ServiceStateProp
) : WebFilter {
    companion object Key

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val next = chain.filter(exchange).contextWrite { it.put(Key, exchange) }

        if (serviceStateProp.circuitBreakerEnable) {
            requestMappingHandlerMapping.getHandler(exchange).share()
                .map { handler ->
                    requestMappingHandlerMapping.handlerMethods.firstOrNull { it.value.toString() == handler.toString() }?.key?.toString() ?: "default"
                }
            // TODO integrate Breaker
//                .flatMap { next.transform { SentinelReactorTransformer(it) } }
        }
        return next
    }
}