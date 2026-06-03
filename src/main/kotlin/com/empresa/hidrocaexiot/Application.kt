package com.empresa.hidrocaexiot

import com.empresa.hidrocaexiot.config.DatabaseFactory
import io.ktor.server.application.Application

fun Application.module() {
    DatabaseFactory.init()
    configureSecurity()
    configureSerialization()
    configureHttp()
    configureStatusPages()
    configureRouting()


}