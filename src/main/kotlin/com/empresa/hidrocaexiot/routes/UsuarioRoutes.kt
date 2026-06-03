package com.empresa.hidrocaexiot.routes

import com.empresa.hidrocaexiot.database.repositories.UsuarioRepository
import com.empresa.hidrocaexiot.models.UsuarioCreateRequest
import com.empresa.hidrocaexiot.models.UsuarioUpdateRequest
import com.empresa.hidrocaexiot.services.AuthService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.usuarioRoutes() {

    authenticate("auth-jwt") {

        route("/usuarios") {

            get {

                call.respond(
                    UsuarioRepository.obtenerTodos()
                )
            }

            get("/{id}") {

                val id =
                    call.parameters["id"]?.toIntOrNull()
                        ?: return@get call.respond(
                            HttpStatusCode.BadRequest
                        )

                val usuario =
                    UsuarioRepository.obtenerPorId(id)
                        ?: return@get call.respond(
                            HttpStatusCode.NotFound
                        )

                call.respond(usuario)
            }

            post {

                val request =
                    call.receive<UsuarioCreateRequest>()

                val usuario =
                    AuthService.registrar(request)

                call.respond(
                    HttpStatusCode.Created,
                    usuario
                )
            }

            put("/{id}") {

                val id =
                    call.parameters["id"]?.toIntOrNull()
                        ?: return@put call.respond(
                            HttpStatusCode.BadRequest
                        )

                val request =
                    call.receive<UsuarioUpdateRequest>()

                val usuario =
                    UsuarioRepository.actualizar(
                        id,
                        request
                    )
                        ?: return@put call.respond(
                            HttpStatusCode.NotFound
                        )

                call.respond(usuario)
            }

            delete("/{id}") {

                val id =
                    call.parameters["id"]?.toIntOrNull()
                        ?: return@delete call.respond(
                            HttpStatusCode.BadRequest
                        )

                val eliminado =
                    UsuarioRepository.eliminar(id)

                if (!eliminado) {
                    return@delete call.respond(
                        HttpStatusCode.NotFound
                    )
                }

                call.respond(
                    HttpStatusCode.OK,
                    mapOf(
                        "mensaje" to "Usuario eliminado"
                    )
                )
            }
        }
    }
}