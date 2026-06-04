package com.empresa.hidrocaexiot.routes

import com.empresa.hidrocaexiot.models.LoginRequest
import com.empresa.hidrocaexiot.models.UsuarioActualResponse
import com.empresa.hidrocaexiot.models.UsuarioCreateRequest
import com.empresa.hidrocaexiot.services.AuthService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {

    route("/auth") {

        post("/register") {

            val request =
                call.receive<UsuarioCreateRequest>()

            val usuario =
                AuthService.registrar(request)

            call.respond(
                HttpStatusCode.Created,
                usuario
            )
        }

        post("/login") {

            val request =
                call.receive<LoginRequest>()

            val response =
                AuthService.login(request)

            call.respond(response)
        }

        authenticate("auth-jwt") {

            get("/me") {

                val principal =
                    call.principal<JWTPrincipal>()
                        ?: return@get call.respond(
                            HttpStatusCode.Unauthorized
                        )

                call.respond(
                    UsuarioActualResponse(
                        userId = principal.payload
                            .getClaim("userId")
                            .asInt(),

                        email = principal.payload
                            .getClaim("email")
                            .asString(),

                        rol = principal.payload
                            .getClaim("rol")
                            .asString()
                    )
                )
            }
        }
    }
}