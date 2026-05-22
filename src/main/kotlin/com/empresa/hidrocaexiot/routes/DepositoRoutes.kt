package com.empresa.hidrocaexiot.routes

import com.empresa.hidrocaexiot.database.repositories.DepositoRepository
import com.empresa.hidrocaexiot.models.DepositoRequest
import com.empresa.hidrocaexiot.models.DepositoUpdateRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.depositoRoutes() {

    route("/depositos") {

        get {
            val depositos = DepositoRepository.obtenerTodos()
            call.respond(HttpStatusCode.OK, depositos)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }

            val deposito = DepositoRepository.obtenerPorId(id)

            if (deposito == null) {
                call.respond(HttpStatusCode.NotFound, "Depósito no encontrado")
                return@get
            }

            call.respond(HttpStatusCode.OK, deposito)
        }

        post {
            val request = call.receive<DepositoRequest>()
            val deposito = DepositoRepository.crear(request)

            call.respond(HttpStatusCode.Created, deposito)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@delete
            }

            val eliminado = DepositoRepository.eliminar(id)

            if (!eliminado) {
                call.respond(HttpStatusCode.NotFound, "Depósito no encontrado")
                return@delete
            }

            call.respond(HttpStatusCode.OK, "Depósito eliminado correctamente")
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@put
            }

            val request = call.receive<DepositoUpdateRequest>()
            val depositoActualizado = DepositoRepository.actualizar(id, request)

            if (depositoActualizado == null) {
                call.respond(HttpStatusCode.NotFound, "Depósito no encontrado")
                return@put
            }

            call.respond(HttpStatusCode.OK, depositoActualizado)
        }
    }
}