package com.empresa.hidrocaexiot.routes

import com.empresa.hidrocaexiot.database.repositories.DepositoRepository
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

            val deposito = DepositoRepository.obtenerPorDeviceEui(request.deviceEui)

            if (deposito == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    "No existe ningún depósito asociado al sensor ${request.deviceEui}"
                )
                return@post
            }

            val distanciaSensorCm = request.distance / 10.0

            val medicionRequest = MedicionRequest(
                depositoId = deposito.id,
                distanciaSensorCm = distanciaSensorCm,
                bateria = request.battery,
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

        post("/debug") {
            val rawBody = call.receiveText()

            println("DEBUG INGESTA BODY:")
            println(rawBody)

            call.respondText(
                text = rawBody,
                contentType = ContentType.Application.Json,
                status = HttpStatusCode.OK
            )
        }
    }
}