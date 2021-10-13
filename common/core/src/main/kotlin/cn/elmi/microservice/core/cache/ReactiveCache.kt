package cn.elmi.microservice.core.cache

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReactiveCache<K, V> {
    fun get(key: K): Mono<V>
    fun put(key: K, value: V): Mono<V>
    fun get(keys: Iterable<K>): Flux<V>
    fun get(keys: Flux<K>): Flux<V>
}