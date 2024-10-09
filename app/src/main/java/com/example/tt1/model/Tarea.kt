package com.example.tt1.model

data class Tarea(
    val id: Int = 0,
    val titulo: String,
    val descripcion: String?,
    val fInicio: String,
    val fVencimiento: String,
    val idEtiqueta: Int,
    val idUsuario: Int // Asegúrate de incluir este campo

)

