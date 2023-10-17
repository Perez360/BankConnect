package com.codex.plugins

import com.codex.util.JacksonUtils
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        register(
            ContentType.Application.Json,
            JacksonConverter(JacksonUtils.getJacksonMapper())
        )
    }
}