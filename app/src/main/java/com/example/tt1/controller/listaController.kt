// ListaTareasController.kt
package com.example.tt1.controller

import com.example.tt1.TareaRepository
import com.example.tt1.model.Tarea
import com.example.tt1.view.ListaTareasView

class ListaTareasController(private val view: ListaTareasView, tareaRepository: TareaRepository) {

    private val tareas = mutableListOf<Tarea>()

    init {
        // Inicializa algunas tareas para simular la base de datos
        inicializarTareas()
    }



    private fun inicializarTareas() {
        tareas.add(Tarea(1, "Tarea 1", "Descripción de la tarea 1", "2024-10-01", "2024-10-05", 1, 1))
        tareas.add(Tarea(2, "Tarea 2", "Descripción de la tarea 2", "2024-10-02", "2024-10-06", 1, 1))
        tareas.add(Tarea(3, "Tarea 3", "Descripción de la tarea 3", "2024-10-03", "2024-10-07", 1, 2))
        // Agrega más tareas según sea necesario
    }

    private fun verTarea(tarea: Tarea) {
        // Lógica para mostrar la vista de detalles de la tarea
        // Esto podría ser un Intent a una nueva actividad que muestre los detalles
        // Por ejemplo, abriendo una nueva actividad llamada DetalleTareaActivity
        // Intent intent = new Intent(context, DetalleTareaActivity.class);
        // intent.putExtra("TAREA_ID", tarea.idTarea);
        // context.startActivity(intent);
    }
}
