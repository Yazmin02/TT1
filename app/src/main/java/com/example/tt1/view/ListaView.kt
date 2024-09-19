package com.example.tt1.view

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.R
import com.example.tt1.model.Tarea

class ListaTareasView(private val rootView: View) {

    private val recyclerViewTareas: RecyclerView = rootView.findViewById(R.id.recyclerViewTareas)

    fun configurarRecyclerView(adapter: RecyclerView.Adapter<*>) {
        recyclerViewTareas.layoutManager = LinearLayoutManager(rootView.context)
        recyclerViewTareas.adapter = adapter
    }
}
