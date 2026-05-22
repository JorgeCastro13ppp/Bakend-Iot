package com.empresa.hidrocaexiot.routes

import com.empresa.hidrocaexiot.database.repositories.DashboardRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.dashboardRoutes() {

    route("/dashboard") {

        get {
            val resumen = DashboardRepository.obtenerResumen()
            call.respond(HttpStatusCode.OK, resumen)
        }
    }
}