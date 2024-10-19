package com.example.tt1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.model.Tarea

class TareaAdapter(
    private val tareas: MutableList<Tarea>, // Cambiado a MutableList
    private val onVerClick: (Tarea) -> Unit,
    private val onEditarClick: (Tarea) -> Unit,
    private val onEliminarClick: (Tarea) -> Unit
) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTarea: TextView = itemView.findViewById(R.id.textTarea)
        val btnVer: Button = itemView.findViewById(R.id.btnVer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tarea_item, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = tareas[position]
        holder.textTarea.text = tarea.titulo

        // Configurar los clics de los botones
        holder.btnVer.setOnClickListener {
            onVerClick(tarea)
        }
    }

    override fun getItemCount(): Int {
        return tareas.size
    }

    // Método para actualizar la lista de tareas
    fun updateTareas(nuevasTareas: List<Tarea>) {
        tareas.clear()
        tareas.addAll(nuevasTareas)
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }
}
