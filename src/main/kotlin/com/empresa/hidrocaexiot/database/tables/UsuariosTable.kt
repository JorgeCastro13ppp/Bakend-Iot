package com.empresa.hidrocaexiot.database.tables

import org.jetbrains.exposed.sql.Table

object UsuariosTable : Table("usuarios") {

    val id = integer("id").autoIncrement()

    val nombre = varchar("nombre", 100)

    val email = varchar("email", 255)
        .uniqueIndex()

    val passwordHash = varchar("password_hash", 255)

    val rol = varchar("rol", 20)

    override val primaryKey = PrimaryKey(id)
}