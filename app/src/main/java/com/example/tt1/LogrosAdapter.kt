package com.example.tt1.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.R

class LogrosAdapter(private val logros: List<String>) :
    RecyclerView.Adapter<LogrosAdapter.LogrosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogrosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_logro, parent, false)
        return LogrosViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogrosViewHolder, position: Int) {
        holder.bind(logros[position])
    }

    override fun getItemCount() = logros.size

    class LogrosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLogro: TextView = itemView.findViewById(R.id.logroTextView)

        fun bind(logro: String) {
            tvLogro.text = logro
        }
    }
}
