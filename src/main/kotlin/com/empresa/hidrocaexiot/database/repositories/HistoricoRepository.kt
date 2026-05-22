package com.empresa.hidrocaexiot.database.repositories

import com.empresa.hidrocaexiot.models.HistoricoDepositoResponse
import com.empresa.hidrocaexiot.models.HistoricoPuntoResponse
import java.time.Instant

object HistoricoRepository {

    fun obtenerHistoricoGeneral(
        desde: Instant?,
        hasta: Instant?
    ): List<HistoricoDepositoResponse> {
        val depositos = DepositoRepository.obtenerTodos()

        return depositos.map { deposito ->
            val mediciones = MedicionRepository.obtenerPorDepositoEntreFechas(
                depositoId = deposito.id,
                desde = desde,
                hasta = hasta
            )

            HistoricoDepositoResponse(
                depositoId = deposito.id,
                nombre = deposito.nombre,
                puntos = mediciones.map {
                    HistoricoPuntoResponse(
                        timestamp = it.timestamp,
                        alturaAguaCm = it.alturaAguaCm,
                        porcentaje = it.porcentaje,
                        litros = it.litros,
                        estado = it.estado,
                        bateria = it.bateria,
                        rssi = it.rssi,
                        snr = it.snr
                    )
                }
            )
        }
    }

    fun obtenerHistoricoDeposito(
        depositoId: Int,
        desde: Instant?,
        hasta: Instant?
    ): HistoricoDepositoResponse? {
        val deposito = DepositoRepository.obtenerPorId(depositoId)
            ?: return null

        val mediciones = MedicionRepository.obtenerPorDepositoEntreFechas(
            depositoId = depositoId,
            desde = desde,
            hasta = hasta
        )

        return HistoricoDepositoResponse(
            depositoId = deposito.id,
            nombre = deposito.nombre,
            puntos = mediciones.map {
                HistoricoPuntoResponse(
                    timestamp = it.timestamp,
                    alturaAguaCm = it.alturaAguaCm,
                    porcentaje = it.porcentaje,
                    litros = it.litros,
                    estado = it.estado,
                    bateria = it.bateria,
                    rssi = it.rssi,
                    snr = it.snr
                )
            }
        )
    }
}