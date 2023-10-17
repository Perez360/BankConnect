package com.codex.web

import com.codex.controllers.BankAccountController
import com.codex.exceptions.ServiceException
import com.codex.models.BankAccount
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.configureBankAccountRouting() {
    val baseUrl = "/api/v1/bank"

    val bankAccountController: BankAccountController by closestDI().instance()
    routing {
        route(baseUrl) {
            post {
                val bankAccount = call.receive<BankAccount>()
                bankAccountController.createBankAccount(bankAccount)
            }

            get("/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest,ServiceException(-4,"No id found"))
                val bankAccount = bankAccountController.getBankAccountById(id)
                call.respond(HttpStatusCode.OK, bankAccount)
            }
        }
    }
}