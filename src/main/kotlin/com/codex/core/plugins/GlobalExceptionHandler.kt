package com.codex.core.plugins



import com.codex.exceptions.ServiceException
import com.codex.util.wrapFailureInResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory
import java.sql.SQLException
import java.time.format.DateTimeParseException
import javax.validation.ConstraintViolationException


fun Application.configureExceptions() {
    val logger = LoggerFactory.getLogger(this::class.java)

    install(StatusPages) {

        exception<NumberFormatException> { call, cause ->
            val message = cause.message ?: "n/a"
            val errorResponse = wrapFailureInResponse<String>(message)

            logger.error("NumberFormatException Error ::: " + cause.stackTraceToString())
            call.respond(
                message = errorResponse,
                status = HttpStatusCode.BadRequest
            )
        }

        exception<DateTimeParseException> { call, cause ->
            val message = cause.message ?: "n/a"
            val errorResponse = wrapFailureInResponse<String>(message)

            logger.error("DateTimeParseException Error ::: " + cause.stackTraceToString())
            call.respond(
                message = errorResponse,
                status = HttpStatusCode.BadRequest
            )
        }
        exception<NullPointerException> { call, cause ->
            val message = cause.message ?: "n/a"
            val errorResponse = wrapFailureInResponse<String>(message)

            logger.error("NullPointerException Error ::: " + cause.stackTraceToString())
            call.respond(
                message = errorResponse,
                status = HttpStatusCode.InternalServerError
            )
        }

        exception<SQLException> { call, cause ->

            val message = cause.message ?: "n/a"
            val errorResponse = wrapFailureInResponse<String>(message)

            logger.error("NullPointerException Error ::: " + cause.stackTraceToString())
            call.respond(
                message = errorResponse,
                status = HttpStatusCode.InternalServerError
            )
        }

        exception<IllegalArgumentException> { call, cause ->
            val message = cause.message ?: "n/a"
            val errorResponse = wrapFailureInResponse<String>(message)

            logger.error("IllegalArgumentException Error ::: " + cause.stackTraceToString())

            call.respond(
                message = errorResponse,
                status = HttpStatusCode.BadRequest
            )
        }

        exception<ConstraintViolationException> { call, cause ->
            val message = cause.message ?: "n/a"
            val errorResponse = wrapFailureInResponse<String>(message)

            logger.error("ConstraintViolationException Error ::: " + cause.stackTraceToString())
            call.respond(
                message = errorResponse,
                status = HttpStatusCode.Conflict
            )
        }

        exception<ServiceException> { call, cause ->

            val message = cause.message ?: "n/a"
            val errorResponse = wrapFailureInResponse<String>(message)
            var httpStatusCode = HttpStatusCode.InternalServerError

            if (cause.code == 0)
                httpStatusCode = HttpStatusCode.OK

            if (cause.code == -2)
                httpStatusCode = HttpStatusCode.BadRequest

            if (cause.code == -3)
                httpStatusCode = HttpStatusCode.NotImplemented

            if (cause.code == -4)
                httpStatusCode = HttpStatusCode.NotModified

            if (cause.code == -5)
                httpStatusCode = HttpStatusCode.FailedDependency

            if (cause.code == -6)
                httpStatusCode = HttpStatusCode.Unauthorized


            logger.error("ServiceException Error ::: " + cause.stackTraceToString())
            call.respond(
                message = errorResponse,
                status = httpStatusCode
            )
        }
    }
}

