package com.example.tt1.model

data class Tarea(
    val idTarea: Int,
    val titulo: String,
    val descripcion: String,
    val fechaInicio: String,
    val fechaVencimiento: String,
    val idUsuario: Int,
    val idEtiqueta: Int
)
