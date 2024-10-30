// RecompensaController.kt
package com.example.tt1.controller

import android.content.Context
import com.example.tt1.model.RecompensaModel

class RecompensaController(context: Context) {
    private val recompensaModel = RecompensaModel(context)

    fun obtenerPuntosTotales(idUsuario: Int): Int {
        return recompensaModel.getPuntosTotales(idUsuario)
    }

    fun obtenerLogros(idUsuario: Int): List<String> {
        return recompensaModel.getLogros(idUsuario)
    }

    fun agregarRecompensa(puntos: Int, idLogro: Int, idUsuario: Int) {
        recompensaModel.agregarRecompensa(puntos, idLogro, idUsuario)
    }
}
