package com.empresa.hidrocaexiot.models

import kotlinx.serialization.Serializable

@Serializable
data class DashboardDepositoResponse(
    val depositoId: Int,
    val nombre: String,
    val alturaCm: Double,
    val alturaAguaCm: Double?,
    val porcentaje: Double?,
    val litros: Double?,
    val estado: EstadoDeposito?,
    val bateria: Int?,
    val rssi: Int?,
    val snr: Double?,
    val timestamp: String?
)