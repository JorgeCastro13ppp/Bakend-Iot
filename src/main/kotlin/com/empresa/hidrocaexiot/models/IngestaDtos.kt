package com.empresa.hidrocaexiot.models

import kotlinx.serialization.Serializable

@Serializable
data class IngestaMedicionRequest(
    val depositoId: Int,
    val distanciaSensorCm: Double,
    val bateria: Int? = null,
    val rssi: Int? = null,
    val snr: Double? = null
)