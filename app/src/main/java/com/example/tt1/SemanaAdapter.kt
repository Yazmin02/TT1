package com.example.tt1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class SemanaAdapter(private val daysOfWeek: List<String>) : RecyclerView.Adapter<SemanaAdapter.WeekViewHolder>() {

    inner class WeekViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayTextView: TextView = view.findViewById(R.id.day_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dia, parent, false)
        return WeekViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        holder.dayTextView.text = daysOfWeek[position]

        // Configura el click listener para cada día
        holder.itemView.setOnClickListener {
            // Aquí podrías abrir una actividad para agregar eventos para el día seleccionado
            Toast.makeText(it.context, "Día seleccionado: ${daysOfWeek[position]}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = daysOfWeek.size
}
