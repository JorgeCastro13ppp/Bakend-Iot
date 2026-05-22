package com.empresa.hidrocaexiot.services

import com.empresa.hidrocaexiot.models.DepositoResponse
import com.empresa.hidrocaexiot.models.EstadoDeposito
import kotlin.math.max
import kotlin.math.min

object NivelService {

    fun calcularAlturaAguaCm(
        alturaDepositoCm: Double,
        distanciaSensorCm: Double,
        offsetSensorCm: Double
    ): Double {
        return max(0.0, alturaDepositoCm - distanciaSensorCm + offsetSensorCm)
    }

    fun calcularPorcentaje(
        alturaAguaCm: Double,
        alturaDepositoCm: Double
    ): Double {
        if (alturaDepositoCm <= 0) return 0.0
        return min(100.0, max(0.0, (alturaAguaCm / alturaDepositoCm) * 100))
    }

    fun calcularLitros(
        largoCm: Double,
        anchoCm: Double,
        alturaAguaCm: Double
    ): Double {
        return (largoCm * anchoCm * alturaAguaCm) / 1000.0
    }

    fun calcularEstado(
        deposito: DepositoResponse,
        alturaAguaCm: Double
    ): EstadoDeposito {
        val distanciaHastaReboseCm = deposito.alturaCm - alturaAguaCm

        return when {
            alturaAguaCm <= deposito.nivelMinimoCriticoCm ->
                EstadoDeposito.NIVEL_MINIMO_CRITICO

            alturaAguaCm <= deposito.nivelMinimoCm ->
                EstadoDeposito.NIVEL_MINIMO

            distanciaHastaReboseCm <= deposito.margenMaximoCriticoCm ->
                EstadoDeposito.NIVEL_MAXIMO_CRITICO

            distanciaHastaReboseCm <= deposito.margenMaximoCm ->
                EstadoDeposito.NIVEL_MAXIMO

            else ->
                EstadoDeposito.NORMAL
        }
    }
}