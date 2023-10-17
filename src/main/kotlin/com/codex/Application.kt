package com.codex

import com.codex.core.config.Configuration
import com.codex.core.config.MongoDatabaseFactory
import com.codex.core.plugins.configureExceptions
import com.codex.core.plugins.configureHTTP
import com.codex.plugins.configureDI
import com.codex.plugins.configureRouting
import com.codex.plugins.configureSerialization
import io.ktor.server.application.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("Application.kt")
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    Configuration.loadSystemProperties()
    MongoDatabaseFactory.connect()

    configureDI()
    configureExceptions()
    configureSerialization()
    configureHTTP()
    configureRouting()
}
