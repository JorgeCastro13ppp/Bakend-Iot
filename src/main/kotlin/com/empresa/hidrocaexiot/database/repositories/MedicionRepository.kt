package com.empresa.hidrocaexiot.database.repositories

import com.empresa.hidrocaexiot.database.tables.MedicionesTable
import com.empresa.hidrocaexiot.models.EstadoDeposito
import com.empresa.hidrocaexiot.models.MedicionRequest
import com.empresa.hidrocaexiot.models.MedicionResponse
import com.empresa.hidrocaexiot.services.NivelService
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

object MedicionRepository {

    fun crear(request: MedicionRequest): MedicionResponse? = transaction {
        val deposito = DepositoRepository.obtenerPorId(request.depositoId)
            ?: return@transaction null

        val alturaAguaCm = NivelService.calcularAlturaAguaCm(
            alturaDepositoCm = deposito.alturaCm,
            distanciaSensorCm = request.distanciaSensorCm,
            offsetSensorCm = deposito.offsetSensorCm
        )

        val porcentaje = NivelService.calcularPorcentaje(
            alturaAguaCm = alturaAguaCm,
            alturaDepositoCm = deposito.alturaCm
        )

        val litros = NivelService.calcularLitros(
            largoCm = deposito.largoCm,
            anchoCm = deposito.anchoCm,
            alturaAguaCm = alturaAguaCm
        )

        val estado = NivelService.calcularEstado(
            deposito = deposito,
            alturaAguaCm = alturaAguaCm
        )

        val now = request.timestamp?.let { Instant.parse(it) } ?: Instant.now()

        val insertStatement = MedicionesTable.insert {
            it[depositoId] = request.depositoId
            it[distanciaSensorCm] = request.distanciaSensorCm
            it[MedicionesTable.alturaAguaCm] = alturaAguaCm
            it[MedicionesTable.porcentaje] = porcentaje
            it[MedicionesTable.litros] = litros
            it[MedicionesTable.estado] = estado.name
            it[bateria] = request.bateria
            it[rssi] = request.rssi
            it[snr] = request.snr
            it[timestamp] = now
        }

        val id = insertStatement[MedicionesTable.id]

        obtenerPorId(id)
    }

    fun obtenerTodas(): List<MedicionResponse> = transaction {
        MedicionesTable
            .selectAll()
            .orderBy(MedicionesTable.timestamp, SortOrder.DESC)
            .map { it.toMedicionResponse() }
    }

    fun obtenerPorDeposito(depositoId: Int): List<MedicionResponse> = transaction {
        MedicionesTable
            .selectAll()
            .where { MedicionesTable.depositoId eq depositoId }
            .orderBy(MedicionesTable.timestamp, SortOrder.DESC)
            .map { it.toMedicionResponse() }
    }

    fun obtenerUltimaPorDeposito(depositoId: Int): MedicionResponse? = transaction {
        MedicionesTable
            .selectAll()
            .where { MedicionesTable.depositoId eq depositoId }
            .orderBy(MedicionesTable.timestamp, SortOrder.DESC)
            .limit(1)
            .map { it.toMedicionResponse() }
            .singleOrNull()
    }

    fun eliminarPorDeposito(depositoId: Int): Int = transaction {
        MedicionesTable
            .deleteWhere { MedicionesTable.depositoId eq depositoId }
    }

    fun eliminarPorId(id: Int): Boolean = transaction {
        val filasEliminadas = MedicionesTable
            .deleteWhere { MedicionesTable.id eq id }

        filasEliminadas > 0
    }

    fun eliminarTodas(): Int = transaction {
        MedicionesTable.deleteAll()
    }

    fun obtenerPorDepositoEntreFechas(
        depositoId: Int,
        desde: Instant?,
        hasta: Instant?
    ): List<MedicionResponse> = transaction {
        var query = MedicionesTable
            .selectAll()
            .where { MedicionesTable.depositoId eq depositoId }

        if (desde != null) {
            query = query.andWhere { MedicionesTable.timestamp greaterEq desde }
        }

        if (hasta != null) {
            query = query.andWhere { MedicionesTable.timestamp lessEq hasta }
        }

        query
            .orderBy(MedicionesTable.timestamp, SortOrder.ASC)
            .map { it.toMedicionResponse() }
    }

    private fun obtenerPorId(id: Int): MedicionResponse? = transaction {
        MedicionesTable
            .selectAll()
            .where { MedicionesTable.id eq id }
            .map { it.toMedicionResponse() }
            .singleOrNull()
    }

    private fun ResultRow.toMedicionResponse(): MedicionResponse {
        return MedicionResponse(
            id = this[MedicionesTable.id],
            depositoId = this[MedicionesTable.depositoId],
            distanciaSensorCm = this[MedicionesTable.distanciaSensorCm],
            alturaAguaCm = this[MedicionesTable.alturaAguaCm],
            porcentaje = this[MedicionesTable.porcentaje],
            litros = this[MedicionesTable.litros],
            estado = EstadoDeposito.valueOf(this[MedicionesTable.estado]),
            bateria = this[MedicionesTable.bateria],
            rssi = this[MedicionesTable.rssi],
            snr = this[MedicionesTable.snr],
            timestamp = this[MedicionesTable.timestamp].toString()
        )
    }
}