package com.empresa.hidrocaexiot.routes

import com.empresa.hidrocaexiot.database.repositories.HistoricoRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Instant

fun Route.historicoRoutes() {

    route("/historico") {

        get("/general") {
            val desde = call.request.queryParameters["desde"]?.let { Instant.parse(it) }
            val hasta = call.request.queryParameters["hasta"]?.let { Instant.parse(it) }

            val historico = HistoricoRepository.obtenerHistoricoGeneral(desde, hasta)
            call.respond(HttpStatusCode.OK, historico)
        }

        get("/deposito/{id}") {
            val depositoId = call.parameters["id"]?.toIntOrNull()

            if (depositoId == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }

            val desde = call.request.queryParameters["desde"]?.let { Instant.parse(it) }
            val hasta = call.request.queryParameters["hasta"]?.let { Instant.parse(it) }

            val historico = HistoricoRepository.obtenerHistoricoDeposito(
                depositoId = depositoId,
                desde = desde,
                hasta = hasta
            )

            if (historico == null) {
                call.respond(HttpStatusCode.NotFound, "Depósito no encontrado")
                return@get
            }

            call.respond(HttpStatusCode.OK, historico)
        }
    }
}