package com.example.tt1.model.entidades

data class Usuario(
    val idUsuario: Int = 0, // Se asigna automáticamente al insertar en la base de datos
    val nUsuario: String,
    val correoE: String, // Ahora coincide con la columna de la tabla en la base de datos
    val password: String // Este también coincide con la columna 'contraseña'
)
