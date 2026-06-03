package com.empresa.hidrocaexiot.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {

    private val secret =
        System.getenv("JWT_SECRET")
            ?: "dev-secret"

    private const val issuer = "hidrocaexiot"

    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier =
        JWT.require(algorithm)
            .withIssuer(issuer)
            .build()

    fun generateToken(
        userId: Int,
        email: String,
        rol: String
    ): String {

        return JWT.create()
            .withIssuer(issuer)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withClaim("rol", rol)
            .withExpiresAt(
                Date(
                    System.currentTimeMillis() + 86400000
                )
            )
            .sign(algorithm)
    }
}