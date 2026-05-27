package com.empresa.hidrocaexiot.database.tables

import org.jetbrains.exposed.sql.Table

object DepositosTable : Table("depositos") {
    val id = integer("id").autoIncrement()
    val nombre = varchar("nombre", 100)

    val deviceEui = varchar("device_eui", 32).nullable().uniqueIndex()

    val alturaCm = double("altura_cm")
    val largoCm = double("largo_cm")
    val anchoCm = double("ancho_cm")
    val offsetSensorCm = double("offset_sensor_cm").default(0.0)

    val nivelMinimoCm = double("nivel_minimo_cm").default(60.0)
    val nivelMinimoCriticoCm = double("nivel_minimo_critico_cm").default(30.0)
    val margenMaximoCm = double("margen_maximo_cm").default(60.0)
    val margenMaximoCriticoCm = double("margen_maximo_critico_cm").default(30.0)

    override val primaryKey = PrimaryKey(id)
}