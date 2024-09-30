package com.example.tt1.model

data class listaTarea(
    val idTarea: Int,
    val titulo: String,
    val desc: String,
    val fInicio: String, // cambiar a formato de fecha en vez de String si es necesario
    val fVencimiento: String, // cambiar a formato de fecha en vez de String si es necesario
    val etiqueta: String,
    val idUsuario: Int // relación con la tabla Usuario
)

