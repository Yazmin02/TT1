// ListaTareasView.kt
package com.example.tt1.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.R
import com.example.tt1.model.Tarea

class ListaTareasView(private var tareas: List<Tarea>, private val itemClick: (Tarea) -> Unit) :
    RecyclerView.Adapter<ListaTareasView.TareaViewHolder>() {

    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.tvTituloTarea)
        val descripcion: TextView = itemView.findViewById(R.id.tvDescripcionTarea)

        fun bind(tarea: Tarea, clickListener: (Tarea) -> Unit) {
            titulo.text = tarea.titulo
            descripcion.text = tarea.descripcion
            itemView.setOnClickListener { clickListener(tarea) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_lista, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = tareas[position]
        holder.bind(tarea, itemClick)
    }

    override fun getItemCount() = tareas.size

    fun mostrarTareas(nuevasTareas: List<Tarea>) {
        tareas = nuevasTareas
        notifyDataSetChanged()
    }

    fun mostrarTareaAgregada(tarea: Tarea) {
        tareas = tareas + tarea
        notifyItemInserted(tareas.size - 1)
    }
}
