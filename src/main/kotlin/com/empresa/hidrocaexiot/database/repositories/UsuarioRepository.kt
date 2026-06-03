package com.empresa.hidrocaexiot.database.repositories

import com.empresa.hidrocaexiot.database.tables.UsuariosTable
import com.empresa.hidrocaexiot.models.UsuarioResponse
import com.empresa.hidrocaexiot.models.UsuarioUpdateRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

data class UsuarioCompleto(
    val id: Int,
    val nombre: String,
    val email: String,
    val passwordHash: String,
    val rol: String
)

object UsuarioRepository {

    fun crear(
        nombre: String,
        email: String,
        passwordHash: String,
        rol: String
    ): UsuarioResponse = transaction {

        val insertStatement = UsuariosTable.insert {

            it[UsuariosTable.nombre] = nombre
            it[UsuariosTable.email] = email.lowercase()
            it[UsuariosTable.passwordHash] = passwordHash
            it[UsuariosTable.rol] = rol
        }

        val id = insertStatement[UsuariosTable.id]

        obtenerPorId(id)!!
    }

    fun obtenerTodos(): List<UsuarioResponse> = transaction {

        UsuariosTable
            .selectAll()
            .map { it.toUsuarioResponse() }
    }

    fun obtenerPorId(id: Int): UsuarioResponse? = transaction {

        UsuariosTable
            .selectAll()
            .where { UsuariosTable.id eq id }
            .map { it.toUsuarioResponse() }
            .singleOrNull()
    }

    fun obtenerCompletoPorId(id: Int): UsuarioCompleto? = transaction {

        UsuariosTable
            .selectAll()
            .where { UsuariosTable.id eq id }
            .map { it.toUsuarioCompleto() }
            .singleOrNull()
    }

    fun obtenerPorEmail(email: String): UsuarioCompleto? = transaction {

        UsuariosTable
            .selectAll()
            .where { UsuariosTable.email eq email.lowercase() }
            .map { it.toUsuarioCompleto() }
            .singleOrNull()
    }

    fun eliminar(id: Int): Boolean = transaction {

        val filasEliminadas =
            UsuariosTable.deleteWhere {
                UsuariosTable.id eq id
            }

        filasEliminadas > 0
    }

    fun actualizar(
        id: Int,
        request: UsuarioUpdateRequest
    ): UsuarioResponse? = transaction {

        val filasActualizadas =
            UsuariosTable.update({
                UsuariosTable.id eq id
            }) {

                it[nombre] = request.nombre
                it[email] = request.email.lowercase()
                it[rol] = request.rol
            }

        if (filasActualizadas == 0)
            null
        else
            obtenerPorId(id)
    }

    private fun ResultRow.toUsuarioResponse(): UsuarioResponse {

        return UsuarioResponse(
            id = this[UsuariosTable.id],
            nombre = this[UsuariosTable.nombre],
            email = this[UsuariosTable.email],
            rol = this[UsuariosTable.rol]
        )
    }

    private fun ResultRow.toUsuarioCompleto(): UsuarioCompleto {

        return UsuarioCompleto(
            id = this[UsuariosTable.id],
            nombre = this[UsuariosTable.nombre],
            email = this[UsuariosTable.email],
            passwordHash = this[UsuariosTable.passwordHash],
            rol = this[UsuariosTable.rol]
        )
    }
}