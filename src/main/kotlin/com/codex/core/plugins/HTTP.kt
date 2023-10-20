package com.codex.core.plugins

import com.codex.domain.UserRequestContext
import com.codex.models.UserRequest
import com.codex.util.JacksonUtils
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        anyHost()
    }

    //To keep track of any request from clients
    install(CallLogging) {
        level = Level.INFO


        filter { call ->
            System.err.println(JacksonUtils.getJacksonMapper().writeValueAsString(call.request.queryParameters.entries()))

            System.err.println("\n${call.request.origin.remoteAddress}: ${call.request.origin.remotePort}")
            call.request.path().startsWith("/")
        }
    }
}
