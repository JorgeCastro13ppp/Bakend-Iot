package com.empresa.hidrocaexiot.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IngestaMedicionRequest(
    val devEUI: String,
    val deviceName: String? = null,
    val distance: Double,
    val battery: Int? = null,
    val rssi: Int? = null,
    val snr: Double? = null,
    val gatewayTime: String? = null,
    val radar_signal_rssi: Double? = null,
    val position: Int? = null
)