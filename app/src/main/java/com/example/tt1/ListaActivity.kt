// ListaTareasActivity.kt
package com.example.tt1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.model.Tarea

class ListaTareasActivity : AppCompatActivity() {

    private lateinit var tareaRepository: TareaRepository
    private lateinit var tareasContainer: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        // Inicializar el repositorio
        tareaRepository = TareaRepository(this)

        // Obtener el ID del usuario que ha iniciado sesión
        val idUsuario = 1 // Reemplaza esto con la lógica para obtener el ID del usuario

        // Referencia al contenedor de tareas
        tareasContainer = findViewById(R.id.tareasContainer)

        // Obtener las tareas del repositorio
        val tareas = tareaRepository.obtenerTareasPorUsuario(idUsuario)

        // Mostrar cada tarea en el contenedor
        for (tarea in tareas) {
            agregarTareaALayout(tarea)
        }
    }

    private fun agregarTareaALayout(tarea: Tarea) {
        // Inflar el layout para una tarea
        val tareaView = LayoutInflater.from(this).inflate(R.layout.activity_lista, null)

        // Referencias a los elementos del layout inflado
        val tvTituloTarea: TextView = tareaView.findViewById(R.id.tvTituloTarea)
        val tvDescripcionTarea: TextView = tareaView.findViewById(R.id.tvDescripcionTarea)

        // Configurar los datos de la tarea
        tvTituloTarea.text = tarea.titulo
        tvDescripcionTarea.text = tarea.descripcion

        // Agregar la vista de la tarea al contenedor
        tareasContainer.addView(tareaView)
    }
}
