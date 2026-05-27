package com.empresa.hidrocaexiot.models

import kotlinx.serialization.Serializable

@Serializable
data class DepositoRequest(
    val nombre: String,
    val deviceEui: String? = null,
    val alturaCm: Double,
    val largoCm: Double,
    val anchoCm: Double,
    val offsetSensorCm: Double = 0.0,
    val nivelMinimoCm: Double = 60.0,
    val nivelMinimoCriticoCm: Double = 30.0,
    val margenMaximoCm: Double = 60.0,
    val margenMaximoCriticoCm: Double = 30.0
)

@Serializable
data class DepositoResponse(
    val id: Int,
    val nombre: String,
    val deviceEui: String?,
    val alturaCm: Double,
    val largoCm: Double,
    val anchoCm: Double,
    val offsetSensorCm: Double,
    val nivelMinimoCm: Double,
    val nivelMinimoCriticoCm: Double,
    val margenMaximoCm: Double,
    val margenMaximoCriticoCm: Double
)
@Serializable
data class DepositoUpdateRequest(
    val nombre: String,
    val deviceEui: String? = null,
    val alturaCm: Double,
    val largoCm: Double,
    val anchoCm: Double,
    val offsetSensorCm: Double = 0.0,
    val nivelMinimoCm: Double = 60.0,
    val nivelMinimoCriticoCm: Double = 30.0,
    val margenMaximoCm: Double = 60.0,
    val margenMaximoCriticoCm: Double = 30.0
)