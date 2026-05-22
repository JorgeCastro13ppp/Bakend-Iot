package com.empresa.hidrocaexiot.routes

import com.empresa.hidrocaexiot.database.repositories.MedicionRepository
import com.empresa.hidrocaexiot.models.MedicionRequest
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.sin
import kotlin.random.Random

fun Route.devRoutes() {

    route("/dev") {

        post("/seed-mediciones") {
            var total = 0

            val dias = 3
            val medicionesPorDia = 24 * 12 // cada 5 minutos
            val totalMedicionesPorDeposito = dias * medicionesPorDia

            val inicio = Instant.now()
                .minus(dias.toLong(), ChronoUnit.DAYS)
                .truncatedTo(ChronoUnit.MINUTES)

            for (depositoId in 1..5) {

                val baseLlenado = when (depositoId) {
                    1 -> 70.0
                    2 -> 55.0
                    3 -> 82.0
                    4 -> 40.0
                    else -> 65.0
                }

                repeat(totalMedicionesPorDeposito) { index ->
                    val timestamp = inicio.plus((index * 5).toLong(), ChronoUnit.MINUTES)

                    val variacionDiaria = sin(index / 35.0) * 12.0
                    val ruido = Random.nextDouble(-3.0, 3.0)

                    val porcentajeSimulado = (baseLlenado + variacionDiaria + ruido)
                        .coerceIn(8.0, 95.0)

                    val alturaDepositoCm = 400.0
                    val alturaAguaCm = alturaDepositoCm * (porcentajeSimulado / 100.0)
                    val distanciaSensorCm = alturaDepositoCm - alturaAguaCm

                    val request = MedicionRequest(
                        depositoId = depositoId,
                        distanciaSensorCm = distanciaSensorCm,
                        bateria = Random.nextInt(70, 100),
                        rssi = Random.nextInt(-110, -75),
                        snr = Random.nextDouble(3.0, 10.0),
                        timestamp = timestamp.toString()
                    )

                    MedicionRepository.crear(request)
                    total++
                }
            }

            call.respond(
                HttpStatusCode.Created,
                "Mediciones generadas: $total"
            )
        }
    }
}