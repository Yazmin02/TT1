package com.example.tt1.evento

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.R
import com.example.tt1.model.entidades.Evento

class EventoAdapter(
    private val eventos: MutableList<Evento>, // Cambiado a MutableList
    private val onVerClick: (Evento) -> Unit,
    private val onEditarClick: (Evento) -> Unit,
    private val onEliminarClick: (Evento) -> Unit
) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textEvento: TextView = itemView.findViewById(R.id.textEvento)
        val btnVer: Button = itemView.findViewById(R.id.btnVer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoAdapter.EventoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_evento, parent, false)
        return EventoViewHolder(view)
    }


    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventos[position]
        holder.textEvento.text = evento.titulo

        // Configurar los clics de los botones
        holder.btnVer.setOnClickListener {
            onVerClick(evento)
        }
    }

    override fun getItemCount(): Int {
        return eventos.size
    }

    // Método para actualizar la lista de tareas
    fun updateEventos(nuevosEventos: List<Evento>) {
        eventos.clear()
        eventos.addAll(nuevosEventos)
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }
}
