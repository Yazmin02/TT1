package com.example.tt1.model

import java.io.Serializable

data class Tarea(
    val id: Int = 0,
    var titulo: String,
    var descripcion: String?,
    val fInicio: String,
    var fVencimiento: String,
    val idEtiqueta: Int,
    val idUsuario: Int // Asegúrate de incluir este campo

): Serializable

