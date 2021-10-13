package cn.elmi.security.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("security")
class SecurityProp {

    var enabled = true
    var whitelisted = emptyList<String>()
    lateinit var services: Map<String, ServiceProp>
    lateinit var component: String

    class ServiceProp {
        lateinit var iss: String
        var conf: String = ""
        var jwks: String = ""
        var desc: String = ""
    }

}