package com.example.tt1.controller

import com.example.tt1.model.PrincipalModel
import com.example.tt1.view.PrincipalView

class PrincipalController(
    private val model: PrincipalModel,
    private val view: PrincipalView
) {

    init {
        mostrarDatos()
    }

    private fun mostrarDatos() {
        view.mostrarTitulo("APLICACIÓN DE GESTIÓN DE TIEMPO")
        view.mostrarTareaPendiente(model.tareasPendientes[0])
        view.mostrarEventoPendiente(model.tareasPendientes[1])
    }
}
