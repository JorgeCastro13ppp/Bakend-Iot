package com.empresa.hidrocaexiot.models

import kotlinx.serialization.Serializable

@Serializable
data class HistoricoPuntoResponse(
    val timestamp: String,
    val alturaAguaCm: Double,
    val porcentaje: Double,
    val litros: Double,
    val estado: EstadoDeposito,
    val bateria: Int?,
    val rssi: Int?,
    val snr: Double?
)

@Serializable
data class HistoricoDepositoResponse(
    val depositoId: Int,
    val nombre: String,
    val puntos: List<HistoricoPuntoResponse>
)