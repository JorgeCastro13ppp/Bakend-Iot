package com.empresa.hidrocaexiot.routes

import com.empresa.hidrocaexiot.database.repositories.MedicionRepository
import com.empresa.hidrocaexiot.models.IngestaMedicionRequest
import com.empresa.hidrocaexiot.models.MedicionRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.ingestaRoutes() {

    route("/ingesta") {

        post("/medicion") {
            val expectedApiKey = call.application.environment.config
                .propertyOrNull("app.apiKey")
                ?.getString()

            val receivedApiKey = call.request.headers["X-API-Key"]

            if (expectedApiKey.isNullOrBlank() || receivedApiKey != expectedApiKey) {
                call.respond(HttpStatusCode.Unauthorized, "API Key inválida")
                return@post
            }

            val request = call.receive<IngestaMedicionRequest>()

            val medicionRequest = MedicionRequest(
                depositoId = request.depositoId,
                distanciaSensorCm = request.distanciaSensorCm,
                bateria = request.bateria,
                rssi = request.rssi,
                snr = request.snr
            )

            val medicion = MedicionRepository.crear(medicionRequest)

            if (medicion == null) {
                call.respond(HttpStatusCode.NotFound, "Depósito no encontrado")
                return@post
            }

            call.respond(HttpStatusCode.Created, medicion)
        }
    }
}