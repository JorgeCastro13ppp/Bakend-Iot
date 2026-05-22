package com.empresa.hidrocaexiot.database.repositories

import com.empresa.hidrocaexiot.models.HistoricoDepositoResponse
import com.empresa.hidrocaexiot.models.HistoricoPuntoResponse

object HistoricoRepository {

    fun obtenerHistoricoGeneral(): List<HistoricoDepositoResponse> {
        val depositos = DepositoRepository.obtenerTodos()

        return depositos.map { deposito ->
            val mediciones = MedicionRepository.obtenerPorDeposito(deposito.id)

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

    fun obtenerHistoricoDeposito(depositoId: Int): HistoricoDepositoResponse? {
        val deposito = DepositoRepository.obtenerPorId(depositoId)
            ?: return null

        val mediciones = MedicionRepository.obtenerPorDeposito(depositoId)

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