package com.empresa.hidrocaexiot.config

import com.empresa.hidrocaexiot.database.tables.DepositosTable
import com.empresa.hidrocaexiot.database.tables.MedicionesTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        val dbHost = System.getenv("DB_HOST") ?: "localhost"
        val dbPort = System.getenv("DB_PORT") ?: "5433"
        val dbName = System.getenv("DB_NAME") ?: "hidrocaex_iot"
        val dbUser = System.getenv("DB_USER") ?: "postgres"
        val dbPassword = System.getenv("DB_PASSWORD") ?: "toor"

        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://$dbHost:$dbPort/$dbName"
            driverClassName = "org.postgresql.Driver"
            username = dbUser
            password = dbPassword
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        Database.connect(HikariDataSource(config))
        transaction {
            SchemaUtils.create(DepositosTable, MedicionesTable)
        }
    }
}