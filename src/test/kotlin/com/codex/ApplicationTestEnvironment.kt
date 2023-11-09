package com.codex

import com.codex.core.config.Configuration
import com.codex.core.config.MongoDatabaseFactory
import com.codex.core.plugins.configureRouting
import com.codex.core.plugins.configureSerialization
import io.ktor.server.application.*


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.moduleTest() {
    Configuration.loadSystemProperties()
    MongoDatabaseFactory.connect()
    configureSerialization()
    configureRouting()
}
