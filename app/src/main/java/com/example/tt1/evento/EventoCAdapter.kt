package com.example.tt1.evento

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.R
import com.example.tt1.model.entidades.Evento

class EventoCAdapter(
    private val eventos: List<Evento>
) : RecyclerView.Adapter<EventoCAdapter.EventoSemanaViewHolder>() {

    class EventoSemanaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textEvento: TextView = itemView.findViewById(R.id.textEvento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoSemanaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.eventoc_item, parent, false)
        return EventoSemanaViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoSemanaViewHolder, position: Int) {
        val evento = eventos[position]
        holder.textEvento.text = evento.titulo
    }

    override fun getItemCount(): Int = eventos.size
}
