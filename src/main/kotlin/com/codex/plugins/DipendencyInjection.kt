package com.codex.plugins

import com.codex.controllers.impl.BankAccountControllerImpl
import com.codex.controllers.impl.UserControllerImpl
import com.codex.core.config.Configuration.getSystemProperties
import com.codex.core.config.MongoDatabaseFactory
import com.codex.repos.BankAccountService
import com.codex.repos.UserService
import dev.morphia.Datastore
import io.ktor.server.application.*
import org.kodein.di.bindConstant
import org.kodein.di.bindSingleton
import org.kodein.di.ktor.di

fun Application.configureDI() {
    val datastore: Datastore = MongoDatabaseFactory.getDataStore()
    di {
        bindConstant(tag = "systemProperties") { getSystemProperties() }

        bindSingleton { UserService(datastore) }
        bindSingleton { UserControllerImpl(di) }

        bindSingleton { BankAccountService(datastore) }
        bindSingleton { BankAccountControllerImpl(di) }
    }
}