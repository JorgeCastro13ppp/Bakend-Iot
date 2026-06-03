package com.empresa.hidrocaexiot

import com.empresa.hidrocaexiot.config.JwtConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {

    install(Authentication) {

        jwt("auth-jwt") {

            realm = "hidrocaexiot"

            verifier(
                JwtConfig.verifier
            )

            validate { credential ->

                val email =
                    credential.payload
                        .getClaim("email")
                        .asString()

                if (email.isNotBlank())
                    JWTPrincipal(
                        credential.payload
                    )
                else
                    null
            }
        }
    }
}