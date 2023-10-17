package com.codex.plugins

import com.codex.web.configureBankAccountRouting
import com.codex.web.configureUserRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText ("Hello World!")
        }
    }

    configureUserRouting()
    configureBankAccountRouting()
}
