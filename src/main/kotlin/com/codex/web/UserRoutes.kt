package com.codex.web

import com.codex.controllers.UserController
import com.codex.exceptions.ServiceException
import com.codex.logger
import com.codex.models.CreateUserDTO
import com.codex.models.FilterUserRequest
import com.codex.models.UpdateUserDTO
import com.codex.util.JacksonUtils
import com.codex.util.validators.UserFilterValidator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.configureUserRouting() {
    val baseUrl = "/api/v1/user"

    val useController: UserController by closestDI().instance()
    routing {
        route(baseUrl) {
            post {
                val newUser = call.receive<CreateUserDTO>()
                val response = useController.createUser(newUser)
                call.respond(HttpStatusCode.OK, response)
            }
            get("/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ServiceException(-4, "No id found")
                )
                val user = useController.getUserById(id)

                call.respond(HttpStatusCode.OK, user)
            }

            get("/list") {
                val page: String? = call.request.queryParameters["page"]
                val size: String? = call.request.queryParameters["size"]

                val response = useController.listUsers(page?.toIntOrNull(), size?.toIntOrNull())
                call.respond(HttpStatusCode.OK, response)
            }

            get("/filter") {
                val parameters: Parameters = call.request.queryParameters

                val map = mutableMapOf<String, Any?>()
                for (x in parameters.entries()) {
                    map[x.key] = x.value.first()
                }

                UserFilterValidator.validate(map)

                val filterUserRequest = FilterUserRequest.fromMap(map)
                val response = useController.filterUsers(filterUserRequest)
                call.respond(HttpStatusCode.OK, response)
            }

            put {
                val updatedUser = call.receive<UpdateUserDTO>()
                val response = useController.updateUser(updatedUser)
                call.respond(HttpStatusCode.OK, response)
            }

            delete("/{id}") {
                val id = call.parameters["id"]
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, ServiceException(-4, "No id found"))

                val response = useController.deleteUser(id)
                call.respond(HttpStatusCode.OK, response)
            }

            delete {
                val response = useController.deleteAllUsers()
                call.respond(HttpStatusCode.OK, response)
            }
        }

    }
}