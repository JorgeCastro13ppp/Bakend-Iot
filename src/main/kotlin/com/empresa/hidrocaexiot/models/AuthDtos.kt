package com.empresa.hidrocaexiot.models

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioActualResponse(
    val userId: Int,
    val email: String,
    val rol: String
)