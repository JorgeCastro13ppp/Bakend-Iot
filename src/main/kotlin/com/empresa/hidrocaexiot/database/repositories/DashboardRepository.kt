package com.empresa.hidrocaexiot.database.repositories

import com.empresa.hidrocaexiot.models.DashboardDepositoResponse

object DashboardRepository {

    fun obtenerResumen(): List<DashboardDepositoResponse> {
        val depositos = DepositoRepository.obtenerTodos()

        return depositos.map { deposito ->
            val ultimaMedicion = MedicionRepository.obtenerUltimaPorDeposito(deposito.id)

            DashboardDepositoResponse(
                depositoId = deposito.id,
                nombre = deposito.nombre,
                alturaCm = deposito.alturaCm,
                alturaAguaCm = ultimaMedicion?.alturaAguaCm,
                porcentaje = ultimaMedicion?.porcentaje,
                litros = ultimaMedicion?.litros,
                estado = ultimaMedicion?.estado,
                bateria = ultimaMedicion?.bateria,
                rssi = ultimaMedicion?.rssi,
                snr = ultimaMedicion?.snr,
                timestamp = ultimaMedicion?.timestamp
            )
        }
    }
}