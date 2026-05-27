package com.empresa.hidrocaexiot.models

import kotlinx.serialization.Serializable

@Serializable
data class IngestaMedicionRequest(
    val deviceEui: String,
    val distance: Double,
    val battery: Int? = null,
    val rssi: Int? = null,
    val snr: Double? = null
)