package com.example.tt1.model

data class Usuario(
    val idUsuario: Int = 0, // Se asigna automáticamente al insertar en la base de datos
    val nUsuario: String,
    val email: String,
    val password: String
)
