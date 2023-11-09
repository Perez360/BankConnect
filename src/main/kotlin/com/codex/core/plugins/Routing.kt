package com.codex.core.plugins

import com.codex.domain.UserRequestContext
import com.codex.logger
import com.codex.models.UserRequest
import com.codex.util.JacksonUtils
import com.codex.web.configureBankAccountRouting
import com.codex.web.configureUserRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    //To generate context of request from clients (users)
    intercept(ApplicationCallPipeline.Setup) {
        val userRequestContext = UserRequest(
            currentLanguage = call.request.headers["language"],
            currentOS = call.request.headers["operatingSystem"],
            currentModelNumber = call.request.headers["modelNumber"],
            currentClientVersion = call.request.headers["clientVersion"],
        )
        UserRequestContext.setUserRequest(userRequestContext)
        logger.info(
            "User Context: " + JacksonUtils.getJacksonMapper().writeValueAsString(userRequestContext)
        )
    }


    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }

    configureUserRouting()
    configureBankAccountRouting()
}
