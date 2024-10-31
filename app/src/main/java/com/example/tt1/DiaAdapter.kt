package com.example.tt1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.model.entidades.Evento
import com.example.tt1.model.entidades.Tarea

class DiaAdapter(private val items: List<Any>) : RecyclerView.Adapter<DiaAdapter.DiaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dia, parent, false)
        return DiaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaViewHolder, position: Int) {
        val item = items[position]
        when (item) {
            is Tarea -> holder.titleTextView.text = item.titulo
            is Evento -> holder.titleTextView.text = item.titulo
        }
    }

    override fun getItemCount(): Int = items.size

    class DiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textTitle)
    }
}
