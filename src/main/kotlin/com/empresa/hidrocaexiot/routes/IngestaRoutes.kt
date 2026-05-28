package com.empresa.hidrocaexiot.routes

import com.empresa.hidrocaexiot.database.repositories.DepositoRepository
import com.empresa.hidrocaexiot.database.repositories.MedicionRepository
import com.empresa.hidrocaexiot.models.IngestaMedicionRequest
import com.empresa.hidrocaexiot.models.MedicionRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.ingestaRoutes() {

    route("/ingesta") {

        post("/debug") {
            val rawBody = call.receiveText()

            println("========== DEBUG INGESTA ==========")
            println("METHOD: ${call.request.httpMethod.value}")
            println("URI: ${call.request.uri}")
            println("HEADERS:")
            call.request.headers.forEach { name, values ->
                println("$name: $values")
            }
            println("BODY:")
            println(rawBody)
            println("===================================")

            call.respondText(
                text = rawBody.ifBlank { """{"message":"Body vacío recibido"}""" },
                contentType = ContentType.Application.Json,
                status = HttpStatusCode.OK
            )
        }

        post("/medicion") {
            val rawBody = call.receiveText()

            println("========== INGESTA MEDICION ==========")
            println("METHOD: ${call.request.httpMethod.value}")
            println("URI: ${call.request.uri}")
            println("HEADERS:")
            call.request.headers.forEach { name, values ->
                println("$name: $values")
            }
            println("BODY:")
            println(rawBody)
            println("======================================")

            val expectedApiKey = call.application.environment.config
                .propertyOrNull("app.apiKey")
                ?.getString()

            val receivedApiKey = call.request.headers["X-API-Key"]

            if (expectedApiKey.isNullOrBlank() || receivedApiKey != expectedApiKey) {
                call.respond(HttpStatusCode.Unauthorized, "API Key inválida")
                return@post
            }

            if (rawBody.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Body vacío")
                return@post
            }

            val request = kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            }.decodeFromString<IngestaMedicionRequest>(rawBody)

            val deposito = DepositoRepository.obtenerPorDeviceEui(request.devEUI)

            if (deposito == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    "No existe ningún depósito asociado al sensor ${request.devEUI}"
                )
                return@post
            }

            val distanciaSensorCm = request.distance / 10.0

            val medicionRequest = MedicionRequest(
                depositoId = deposito.id,
                distanciaSensorCm = distanciaSensorCm,
                bateria = request.battery,
                rssi = request.rssi,
                snr = request.snr,
                deviceEui = request.devEUI,
                deviceName = request.deviceName,
                gatewayTime = request.gatewayTime,
                radarSignalRssi = request.radar_signal_rssi,
                position = request.position
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