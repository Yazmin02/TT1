package com.example.tt1.model

import com.example.tt1.model.entidades.Tarea

data class PrincipalModel(
    val tareasPendientes: List<Tarea> = listOf() // lista de objetos Tarea
)
