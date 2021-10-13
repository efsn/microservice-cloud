package cn.elmi.security.authentication

import cn.elmi.security.configuration.SecurityProp
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

class AuthenticationService(val securityProp: SecurityProp) {
    private val ANY = "(\\S+|\\*)"
    private val PREFIX = "SCOPE_"
    private val regExs = WeakHashMap<String, Regex>()

    fun hasAccess4User(uuid: String) = getConnectedUUID() == uuid || isService()

    fun hasAccess4Brand(brand: String) = hasAccess(brand = brand)

    fun hasAccess(component: String = "", brand: String = "", resource: String = "", action: String = "") =
        getAuthentication().authorities.map { it.authority }.any { matches(component, brand, resource, action, it) }

    fun isService() = getAuthentication().authorities.map { it.authority }.any { it == "${PREFIX}SERVICE" }

    fun matches(component: String, brand: String, resource: String, action: String, authority: String): Boolean {
        val regEx = "$PREFIX${
            buildAttribute(
                component,
                securityProp.component
            )
        }\\.${buildAttribute(brand)}:${buildAttribute(resource)}\\.${buildAttribute(action)}"

        return regExs.computeIfAbsent(regEx) { it.toRegex(RegexOption.IGNORE_CASE) }.matches(authority)
    }

    private fun buildAttribute(attr: String, prop: String = "") = if (attr.isNotBlank()) "($attr)|\\*" else if (prop.isNotBlank()) "($prop|\\*)" else ANY

    private fun getConnectedUUID() = getAuthentication().name

    fun getAuthentication() = SecurityContextHolder.getContext().authentication
}