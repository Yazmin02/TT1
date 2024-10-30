package com.example.tt1.controller

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.R
import com.example.tt1.model.TareaModel
import com.example.tt1.model.entidades.Tarea
import com.example.tt1.model.repositorios.TareaRepository
import com.example.tt1.view.TareaView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareaController(private val tareaRepository: TareaRepository) : AppCompatActivity() {

    private lateinit var tareaView: TareaView
    private val tareaModel = TareaModel()
    private lateinit var tarea: Tarea // Define la tarea que se va a editar/completar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarea)

        // Inicializa la vista
        tareaView = TareaView(findViewById(android.R.id.content))

        // Configura el Spinner de etiquetas
        tareaView.configurarSpinner(this, tareaModel.categorias)

        // Muestra el DatePicker cuando el campo de fecha es clicado
        tareaView.mostrarDatePicker(this)

        // Configura el listener para el botón de guardar tarea
        tareaView.setOnGuardarTareaListener { guardarTarea() }

        tareaView.setOnCompletarTareaListener { completarTarea() }

    }

    private fun guardarTarea() {
        val titulo = tareaView.obtenerTitulo()
        val descripcion = tareaView.obtenerDescripcion()
        val fecha = tareaView.obtenerFecha()

        // Aquí debes manejar la lógica para obtener idUsuario y idEtiqueta si es necesario
        val idUsuario = 1 // Cambia esto según tu lógica de usuario
        val idEtiqueta = tareaView.obtenerEtiqueta()

        // Lógica para validar datos y guardar la tarea
        if (titulo.isNotEmpty() && descripcion.isNotEmpty() && fecha.isNotEmpty()) {
            // Crea la tarea
            val tarea = Tarea(0, titulo, descripcion, fecha, fecha, 0, idEtiqueta, idUsuario)

            // Guardado asíncrono
            CoroutineScope(Dispatchers.IO).launch {
                tareaRepository.agregarTarea(tarea) // Cambiado a insertarTarea
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TareaController, "Tarea guardada exitosamente", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Muestra mensaje de error
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
    private fun completarTarea() {
        // Crea una copia de la tarea con el estado actualizado
        val tareaCompletada = tarea.copy(estado = 1) // Suponiendo que 1 indica que la tarea está completada

        CoroutineScope(Dispatchers.IO).launch {
            tareaRepository.actualizarTarea(tareaCompletada)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@TareaController, "Tarea completada", Toast.LENGTH_SHORT).show()
                finish() // Cierra la actividad o redirige según sea necesario
            }
        }
    }
}
