package com.empresa.hidrocaexiot.models

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioCreateRequest(
    val nombre: String,
    val email: String,
    val password: String,
    val rol: String
)

@Serializable
data class UsuarioResponse(
    val id: Int,
    val nombre: String,
    val email: String,
    val rol: String
)

@Serializable
data class UsuarioUpdateRequest(
    val nombre: String,
    val email: String,
    val rol: String
)
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String
)