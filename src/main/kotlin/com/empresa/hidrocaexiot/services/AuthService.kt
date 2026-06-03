package com.empresa.hidrocaexiot.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.empresa.hidrocaexiot.config.JwtConfig
import com.empresa.hidrocaexiot.database.repositories.UsuarioRepository
import com.empresa.hidrocaexiot.models.*

object AuthService {

    fun registrar(
        request: UsuarioCreateRequest
    ): UsuarioResponse {

        val email =
            request.email
                .trim()
                .lowercase()

        if (request.password.length < 8) {
            throw IllegalArgumentException(
                "La contraseña debe tener al menos 8 caracteres"
            )
        }

        val existente =
            UsuarioRepository.obtenerPorEmail(email)

        if (existente != null) {
            throw IllegalArgumentException(
                "Ya existe un usuario con ese email"
            )
        }

        val passwordHash =
            BCrypt.withDefaults()
                .hashToString(
                    12,
                    request.password.toCharArray()
                )

        return UsuarioRepository.crear(
            nombre = request.nombre.trim(),
            email = email,
            passwordHash = passwordHash,
            rol = request.rol.uppercase()
        )
    }

    fun login(
        request: LoginRequest
    ): LoginResponse {

        val usuario =
            UsuarioRepository.obtenerPorEmail(
                request.email
                    .trim()
                    .lowercase()
            )
                ?: throw IllegalArgumentException(
                    "Credenciales incorrectas"
                )

        val passwordValida =
            BCrypt.verifyer()
                .verify(
                    request.password.toCharArray(),
                    usuario.passwordHash
                )
                .verified

        if (!passwordValida) {
            throw IllegalArgumentException(
                "Credenciales incorrectas"
            )
        }

        val token =
            JwtConfig.generateToken(
                userId = usuario.id,
                email = usuario.email,
                rol = usuario.rol
            )

        return LoginResponse(token)
    }
}