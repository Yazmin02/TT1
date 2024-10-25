package com.example.tt1.controller

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.R
import com.example.tt1.model.entidades.Evento
import com.example.tt1.model.EventoModel
import com.example.tt1.model.repositorios.EventoRepository
import com.example.tt1.view.EventoView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventoController(private val eventoRepository: EventoRepository) : AppCompatActivity() {

    private lateinit var eventoView: EventoView
    private val eventoModel = EventoModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento)

        // Inicializa la vista
        eventoView = EventoView(findViewById(android.R.id.content))

        // Configura el Spinner de etiquetas
        eventoView.configurarSpinner(this, eventoModel.categorias)

        // Muestra el DatePicker cuando el campo de fecha es clicado
        eventoView.mostrarDatePicker(this)

        // Configura el listener para el botón de guardar evento
        eventoView.setOnGuardarEventoListener { guardarEvento() }
    }

    private fun guardarEvento() {
        val titulo = eventoView.obtenerTitulo()
        val descripcion = eventoView.obtenerDescripcion()
        val fecha = eventoView.obtenerFecha()
        val lugar = eventoView.obtenerLugar()

        // Aquí debes manejar la lógica para obtener idUsuario y idEtiqueta si es necesario
        val idUsuario = 1 // Cambia esto según tu lógica de usuario
        val idEtiqueta = eventoView.obtenerEtiqueta()

        // Lógica para validar datos y guardar la tarea
        if (titulo.isNotEmpty() && descripcion.isNotEmpty() && fecha.isNotEmpty()) {
            // Crea la tarea
            val evento = Evento(0, titulo, descripcion, fecha, fecha, idEtiqueta ,idUsuario, lugar)

            // Guardado asíncrono
            CoroutineScope(Dispatchers.IO).launch {
                eventoRepository.agregarEvento(evento) // Cambiado a insertarTarea
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EventoController, "Evento guardado exitosamente", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Muestra mensaje de error
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}
