package cn.elmi.security.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke

@ConditionalOnProperty(prefix = "security", name = ["enabled"], havingValue = "false")
@EnableWebSecurity
class SecurityDisabledAutoConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http {
            csrf { disable() }
            authorizeRequests {
                authorize(access = permitAll)
            }
        }
    }

}