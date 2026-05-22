package com.empresa.hidrocaexiot.routes

import com.empresa.hidrocaexiot.database.repositories.MedicionRepository
import com.empresa.hidrocaexiot.models.MedicionRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.medicionRoutes() {

    route("/mediciones") {

        get {
            val mediciones = MedicionRepository.obtenerTodas()
            call.respond(HttpStatusCode.OK, mediciones)
        }

        post {
            val request = call.receive<MedicionRequest>()
            val medicion = MedicionRepository.crear(request)

            if (medicion == null) {
                call.respond(HttpStatusCode.NotFound, "Depósito no encontrado")
                return@post
            }

            call.respond(HttpStatusCode.Created, medicion)
        }

        get("/deposito/{id}") {
            val depositoId = call.parameters["id"]?.toIntOrNull()

            if (depositoId == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }

            val mediciones = MedicionRepository.obtenerPorDeposito(depositoId)
            call.respond(HttpStatusCode.OK, mediciones)
        }

        get("/deposito/{id}/ultima") {
            val depositoId = call.parameters["id"]?.toIntOrNull()

            if (depositoId == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }

            val medicion = MedicionRepository.obtenerUltimaPorDeposito(depositoId)

            if (medicion == null) {
                call.respond(HttpStatusCode.NotFound, "No hay mediciones para este depósito")
                return@get
            }

            call.respond(HttpStatusCode.OK, medicion)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@delete
            }

            val eliminado = MedicionRepository.eliminarPorId(id)

            if (!eliminado) {
                call.respond(HttpStatusCode.NotFound, "Medición no encontrada")
                return@delete
            }

            call.respond(HttpStatusCode.OK, "Medición eliminada correctamente")
        }

        delete {
            val eliminadas = MedicionRepository.eliminarTodas()
            call.respond(HttpStatusCode.OK, "Mediciones eliminadas: $eliminadas")
        }
    }
}