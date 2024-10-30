// Evento.kt
package com.example.tt1.model.entidades

data class Evento(
    var idEvento: Int = 0,
    var titulo: String,
    var descripcion: String,
    var fInicio: String, // Formato "YYYY-MM-DD"
    var fVencimiento: String, // Formato "YYYY-MM-DD"
    var estado: Int=0,
    val idEtiqueta: Int,
    val idUsuario: Int, // Asegúrate de incluir este campo
    var lugar: String
)




