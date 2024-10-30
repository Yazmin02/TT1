package com.example.tt1.model.entidades

import java.io.Serializable

data class Tarea(
    val id: Int = 0,
    var titulo: String,
    var descripcion: String?,
    val fInicio: String,
    var fVencimiento: String,
    var estado: Int=0,
    val idEtiqueta: Int,
    val idUsuario: Int // Asegúrate de incluir este campo

): Serializable