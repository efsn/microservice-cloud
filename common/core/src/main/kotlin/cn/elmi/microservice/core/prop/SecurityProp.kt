package cn.elmi.microservice.core.prop

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.security")
class SecurityProp {
    lateinit var rsaPublicKey: String
    lateinit var signatureSalt: String
    lateinit var aesKey: String
}