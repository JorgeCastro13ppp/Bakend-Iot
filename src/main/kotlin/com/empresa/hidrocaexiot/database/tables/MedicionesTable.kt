package com.empresa.hidrocaexiot.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object MedicionesTable : Table("mediciones") {
    val id = integer("id").autoIncrement()
    val depositoId = integer("deposito_id").references(DepositosTable.id)

    val distanciaSensorCm = double("distancia_sensor_cm")
    val alturaAguaCm = double("altura_agua_cm")
    val porcentaje = double("porcentaje")
    val litros = double("litros")
    val estado = varchar("estado", 50)

    val bateria = integer("bateria").nullable()
    val rssi = integer("rssi").nullable()
    val snr = double("snr").nullable()

    val timestamp = timestamp("timestamp")

    override val primaryKey = PrimaryKey(id)
}