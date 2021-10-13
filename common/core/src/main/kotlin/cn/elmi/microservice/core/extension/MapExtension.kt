package cn.elmi.microservice.core.extension

fun <K, V> Map<K, V>.firstOrNull(predicate: (Map.Entry<K, V>) -> Boolean): Map.Entry<K, V>? {
    for (entry in this) {
        if (predicate(entry)) {
            return entry
        }
    }
    return null
}