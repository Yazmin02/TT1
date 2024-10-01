package com.example.tt1.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.R
import com.example.tt1.TareaRepository
import com.example.tt1.model.Tarea
import com.example.tt1.model.TareaModel
import com.example.tt1.view.TareaView

class TareaController(private val tareaRepository: TareaRepository) : AppCompatActivity() {

    private lateinit var tareaView: TareaView
    private val tareaModel = TareaModel()

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
    }

    private fun guardarTarea() {
        val titulo = tareaView.obtenerTitulo()
        val descripcion = tareaView.obtenerDescripcion()
        val fecha = tareaView.obtenerFecha()

        // Aquí debes manejar la lógica para obtener idUsuario y idEtiqueta si es necesario
        val idUsuario = 1 // Cambia esto según tu lógica de usuario
        val idEtiqueta = tareaView.obtenerEtiqueta() // Suponiendo que tienes un método para obtener la etiqueta seleccionada

        // Lógica para validar datos y guardar la tarea
        if (titulo.isNotEmpty() && descripcion.isNotEmpty() && fecha.isNotEmpty()) {
            // Crea la tarea y la agrega al repositorio
            val tarea = Tarea(0, titulo, descripcion, fecha, fecha, idUsuario, idEtiqueta)
            tareaRepository.agregarTarea(tarea)
            // Muestra mensaje de éxito
        } else {
            // Muestra mensaje de error
        }
    }
}
