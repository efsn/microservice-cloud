package cn.elmi.security.error

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import java.nio.charset.StandardCharsets.UTF_8
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

data class APIError(
    val uri: String,
    val message: String,
    val traceId: String = "TODO",
    val datetime: LocalDateTime
)

abstract class BaseSecurityErrorResponseWriter(private val objectMapper: ObjectMapper) {
    fun write(request: HttpServletRequest, response: HttpServletResponse, message: String, httpStatus: HttpStatus) =
        response.apply {
            status = httpStatus.value()
            contentType = MediaType.APPLICATION_JSON_VALUE
            characterEncoding = UTF_8.name()
            writer.write(
                objectMapper.writeValueAsString(
                    APIError(
                        uri = request.requestURI,
                        message = message,
                        datetime = LocalDateTime.now()
                    )
                )
            )
        }
}

class ForbiddenHandler(objectMapper: ObjectMapper) : BaseSecurityErrorResponseWriter(objectMapper), AccessDeniedHandler {
    override fun handle(request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException) {
        write(request, response, accessDeniedException.message.orEmpty(), HttpStatus.FORBIDDEN)
    }
}

class UnauthorizedEntryPoint(objectMapper: ObjectMapper) : BaseSecurityErrorResponseWriter(objectMapper), AuthenticationEntryPoint {
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        write(request, response, authException.message.orEmpty(), HttpStatus.UNAUTHORIZED)
    }
}