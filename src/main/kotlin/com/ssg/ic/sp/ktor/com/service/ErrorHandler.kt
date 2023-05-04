package com.ssg.ic.sp.ktor.com.service

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import java.time.Instant

fun StatusPagesConfig.defaultExceptionHandler() {
    exception<Exception> { call, cause ->
        call.respond(HttpStatusCode.BadRequest, ErrorResponse(message = cause.message))
    }
}

fun toHttpStatus(code: Int): HttpStatusCode {
    return try {
        HttpStatusCode.fromValue(code)
    } catch (e: Exception) {
        HttpStatusCode.BadRequest
    }
}

data class ErrorResponse @JsonCreator constructor(
    @JsonProperty("code")
    val code: Int? = null,
    @JsonProperty("message")
    val message: String?,
    @JsonProperty("timestamp")
    val timestamp: Instant = Instant.now()
)