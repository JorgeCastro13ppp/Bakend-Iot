package com.empresa.hidrocaexiot

import com.empresa.hidrocaexiot.routes.dashboardRoutes
import com.empresa.hidrocaexiot.routes.depositoRoutes
import com.empresa.hidrocaexiot.routes.devRoutes
import com.empresa.hidrocaexiot.routes.historicoRoutes
import com.empresa.hidrocaexiot.routes.ingestaRoutes
import com.empresa.hidrocaexiot.routes.medicionRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/json/kotlinx-serialization") {
            call.respondText("Hidrocaex IoT Backend funcionando")        }

        depositoRoutes()
        medicionRoutes()
        dashboardRoutes()
        historicoRoutes()
        ingestaRoutes()
        devRoutes()
    }
}