package com.example.tt1

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.model.Tarea

class ListaActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tareaAdapter: TareaAdapter
    private var tareas: List<Tarea> = listOf() // Inicializa tu lista de tareas

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializa el adapter
        tareaAdapter = TareaAdapter(tareas, this) { tarea ->
            // Aquí puedes definir la acción al hacer clic en una tarea
            // Por ejemplo, abrir un dialogo para editar o ver la tarea
        }

        recyclerView.adapter = tareaAdapter
    }

    // Clase interna para el Adapter
    inner class TareaAdapter(
        private var tareas: List<Tarea>,
        private val context: Context,
        private val onTareaClick: (Tarea) -> Unit
    ) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.activity_lista, parent, false)
            return TareaViewHolder(view)
        }

        override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
            val tarea = tareas[position]
            holder.bind(tarea)
            holder.itemView.setOnClickListener { onTareaClick(tarea) }
        }

        override fun getItemCount(): Int {
            return tareas.size
        }

        inner class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val txtTitulo: TextView = itemView.findViewById(R.id.txtTitulo)
            private val txtDescripcion: TextView = itemView.findViewById(R.id.txtDescripcion)

            fun bind(tarea: Tarea) {
                txtTitulo.text = tarea.titulo
                txtDescripcion.text = tarea.descripcion
            }
        }
    }
}
