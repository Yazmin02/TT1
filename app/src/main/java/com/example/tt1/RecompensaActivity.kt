// RecompensaActivity.kt
package com.example.tt1

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.R
import com.example.tt1.controller.RecompensaController

class RecompensaActivity : AppCompatActivity() {

    private lateinit var recompensaController: RecompensaController
    private lateinit var tvPuntosTotales: TextView
    private lateinit var lvLogros: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recompensa)

        val idUsuario = 1 // ID de ejemplo; deberías obtenerlo del usuario actual en la app

        recompensaController = RecompensaController(this)
        tvPuntosTotales = findViewById(R.id.tvPuntosTotales)
        lvLogros = findViewById(R.id.lvLogros)

        mostrarPuntosTotales(idUsuario)
        mostrarLogros(idUsuario)
    }

    private fun mostrarPuntosTotales(idUsuario: Int) {
        val puntosTotales = recompensaController.obtenerPuntosTotales(idUsuario)
        tvPuntosTotales.text = "Puntos Totales: $puntosTotales"
    }

    private fun mostrarLogros(idUsuario: Int) {
        val logros = recompensaController.obtenerLogros(idUsuario)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, logros)
        lvLogros.adapter = adapter
    }
}
