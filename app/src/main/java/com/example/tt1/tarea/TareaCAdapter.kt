package com.example.tt1.tarea

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.R
import com.example.tt1.model.entidades.Tarea

class TareaCAdapter(
    private val tareas: List<Tarea>
) : RecyclerView.Adapter<TareaCAdapter.TareaSemanaViewHolder>() {

    class TareaSemanaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTarea: TextView = itemView.findViewById(R.id.textTarea)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaSemanaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tareac_item, parent, false)
        return TareaSemanaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaSemanaViewHolder, position: Int) {
        val tarea = tareas[position]
        holder.textTarea.text = tarea.titulo
    }

    override fun getItemCount(): Int = tareas.size
}
