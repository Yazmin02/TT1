package com.example.tt1.view

import android.app.Activity
import android.widget.TextView
import com.example.tt1.R
import com.example.tt1.model.Tarea

class PrincipalView(private val activity: Activity) {

    private val tituloTextView: TextView = activity.findViewById(R.id.titulo_text_view)
    private val tareaPendienteTextView: TextView = activity.findViewById(R.id.tarea_pendiente_text_view)
    private val eventoPendienteTextView: TextView = activity.findViewById(R.id.evento_pendiente_text_view)

    fun mostrarTitulo(titulo: String) {
        tituloTextView.text = titulo
    }

    fun mostrarTareaPendiente(tarea: Tarea) {
        tareaPendienteTextView.text = tarea.toString()
    }

    fun mostrarEventoPendiente(evento: Tarea) {
        eventoPendienteTextView.text = evento.toString()
    }
}