package com.empresa.hidrocaexiot.database.repositories

import com.empresa.hidrocaexiot.database.tables.DepositosTable
import com.empresa.hidrocaexiot.models.DepositoRequest
import com.empresa.hidrocaexiot.models.DepositoResponse
import com.empresa.hidrocaexiot.models.DepositoUpdateRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object DepositoRepository {

    fun crear(request: DepositoRequest): DepositoResponse = transaction {
        val insertStatement = DepositosTable.insert {
            it[nombre] = request.nombre
            it[deviceEui] = request.deviceEui?.uppercase()
            it[alturaCm] = request.alturaCm
            it[largoCm] = request.largoCm
            it[anchoCm] = request.anchoCm
            it[offsetSensorCm] = request.offsetSensorCm
            it[nivelMinimoCm] = request.nivelMinimoCm
            it[nivelMinimoCriticoCm] = request.nivelMinimoCriticoCm
            it[margenMaximoCm] = request.margenMaximoCm
            it[margenMaximoCriticoCm] = request.margenMaximoCriticoCm
        }

        val id = insertStatement[DepositosTable.id]
        obtenerPorId(id)!!
    }

    fun obtenerTodos(): List<DepositoResponse> = transaction {
        DepositosTable
            .selectAll()
            .map { it.toDepositoResponse() }
    }

    fun obtenerPorId(id: Int): DepositoResponse? = transaction {
        DepositosTable
            .selectAll()
            .where { DepositosTable.id eq id }
            .map { it.toDepositoResponse() }
            .singleOrNull()
    }

    fun obtenerPorDeviceEui(deviceEui: String): DepositoResponse? = transaction {
        DepositosTable
            .selectAll()
            .where { DepositosTable.deviceEui eq deviceEui.uppercase() }
            .map { it.toDepositoResponse() }
            .singleOrNull()
    }

    fun eliminar(id: Int): Boolean = transaction {
        MedicionRepository.eliminarPorDeposito(id)

        val filasEliminadas = DepositosTable
            .deleteWhere { DepositosTable.id eq id }

        filasEliminadas > 0
    }

    fun actualizar(id: Int, request: DepositoUpdateRequest): DepositoResponse? = transaction {
        val filasActualizadas = DepositosTable.update({ DepositosTable.id eq id }) {
            it[nombre] = request.nombre
            it[deviceEui] = request.deviceEui?.uppercase()
            it[alturaCm] = request.alturaCm
            it[largoCm] = request.largoCm
            it[anchoCm] = request.anchoCm
            it[offsetSensorCm] = request.offsetSensorCm
            it[nivelMinimoCm] = request.nivelMinimoCm
            it[nivelMinimoCriticoCm] = request.nivelMinimoCriticoCm
            it[margenMaximoCm] = request.margenMaximoCm
            it[margenMaximoCriticoCm] = request.margenMaximoCriticoCm
        }

        if (filasActualizadas == 0) null else obtenerPorId(id)
    }

    private fun ResultRow.toDepositoResponse(): DepositoResponse {
        return DepositoResponse(
            id = this[DepositosTable.id],
            nombre = this[DepositosTable.nombre],
            deviceEui = this[DepositosTable.deviceEui],
            alturaCm = this[DepositosTable.alturaCm],
            largoCm = this[DepositosTable.largoCm],
            anchoCm = this[DepositosTable.anchoCm],
            offsetSensorCm = this[DepositosTable.offsetSensorCm],
            nivelMinimoCm = this[DepositosTable.nivelMinimoCm],
            nivelMinimoCriticoCm = this[DepositosTable.nivelMinimoCriticoCm],
            margenMaximoCm = this[DepositosTable.margenMaximoCm],
            margenMaximoCriticoCm = this[DepositosTable.margenMaximoCriticoCm]
        )
    }
}