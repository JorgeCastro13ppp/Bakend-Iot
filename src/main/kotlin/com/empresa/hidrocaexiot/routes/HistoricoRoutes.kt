package com.empresa.hidrocaexiot.routes

import com.empresa.hidrocaexiot.database.repositories.HistoricoRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.historicoRoutes() {

    route("/historico") {

        get("/general") {
            val historico = HistoricoRepository.obtenerHistoricoGeneral()
            call.respond(HttpStatusCode.OK, historico)
        }

        get("/deposito/{id}") {
            val depositoId = call.parameters["id"]?.toIntOrNull()

            if (depositoId == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }

            val historico = HistoricoRepository.obtenerHistoricoDeposito(depositoId)

            if (historico == null) {
                call.respond(HttpStatusCode.NotFound, "Depósito no encontrado")
                return@get
            }

            call.respond(HttpStatusCode.OK, historico)
        }
    }
}