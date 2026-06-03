package com.empresa.hidrocaexiot

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {

    install(StatusPages) {

        exception<IllegalArgumentException> { call, cause ->

            call.respond(
                HttpStatusCode.BadRequest,
                mapOf(
                    "error" to (cause.message ?: "Solicitud inválida")
                )
            )
        }

        exception<Throwable> { call, cause ->

            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf(
                    "error" to "Error interno del servidor",
                    "detalle" to cause.message
                )
            )
        }
    }
}