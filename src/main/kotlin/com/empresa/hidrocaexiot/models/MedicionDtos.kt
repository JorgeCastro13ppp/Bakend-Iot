package com.empresa.hidrocaexiot.models

import kotlinx.serialization.Serializable

@Serializable
data class MedicionRequest(
    val depositoId: Int,
    val distanciaSensorCm: Double,
    val bateria: Int? = null,
    val rssi: Int? = null,
    val snr: Double? = null,
    val timestamp: String? = null,

    val deviceEui: String? = null,
    val deviceName: String? = null,
    val gatewayTime: String? = null,
    val radarSignalRssi: Double? = null,
    val position: Int? = null
)

@Serializable
data class MedicionResponse(
    val id: Int,
    val depositoId: Int,
    val distanciaSensorCm: Double,
    val alturaAguaCm: Double,
    val porcentaje: Double,
    val litros: Double,
    val estado: EstadoDeposito,
    val bateria: Int?,
    val rssi: Int?,
    val snr: Double?,
    val timestamp: String,
    val deviceEui: String? = null,
    val deviceName: String? = null,
    val gatewayTime: String? = null,
    val radarSignalRssi: Double? = null,
    val position: Int? = null
)