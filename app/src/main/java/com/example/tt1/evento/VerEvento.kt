package com.example.tt1.evento

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.DatabaseHelper
import com.example.tt1.R
import com.example.tt1.model.repositorios.EventoRepository

class VerEventoActivity : AppCompatActivity() {

    private lateinit var tituloTextView: TextView
    private lateinit var descripcionTextView: TextView
    private lateinit var fInicioTextView: TextView
    private lateinit var fVencimientoTextView: TextView
    private lateinit var etiquetaTextView: TextView
    private lateinit var ubicacionTextView: TextView // Agregado para ubicación
    private lateinit var botonEditar: Button
    private lateinit var botonEliminar: Button
    private lateinit var eventoRepository: EventoRepository
    private var idEvento: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ver_evento)

        inicializarVistas()
        eventoRepository = EventoRepository(DatabaseHelper(this))

        // Obtener el ID de la tarea del Intent
        idEvento = intent.getIntExtra("idEvento", -1)

        // Cargar los detalles de la tarea
        cargarDetallesEvento()

        configurarEventos()
    }

    override fun onResume() {
        super.onResume()
        // Actualizar los detalles de la tarea cada vez que la actividad se reanuda
        cargarDetallesEvento()
    }

    private fun inicializarVistas() {
        tituloTextView = findViewById(R.id.tituloTextView)
        descripcionTextView = findViewById(R.id.descripcionTextView)
        fInicioTextView = findViewById(R.id.fInicioTextView)
        fVencimientoTextView = findViewById(R.id.fVencimientoTextView)
        etiquetaTextView = findViewById(R.id.etiquetaTextView)
        ubicacionTextView = findViewById(R.id.ubicacionTextView) // Inicializa el TextView de ubicación
        botonEditar = findViewById(R.id.botonEditar)
        botonEliminar = findViewById(R.id.botonEliminar)
    }

    private fun configurarEventos() {
        botonEditar.setOnClickListener {
            if (idEvento != -1) {
                val intent = Intent(this, EditarEvento::class.java).apply {
                    putExtra("idEvento", idEvento)
                }
                startActivity(intent)
            } else {
                mostrarMensajeError("ID de evento no válido")
            }
        }

        botonEliminar.setOnClickListener {
            mostrarDialogoConfirmacion()
        }
    }

    private fun cargarDetallesEvento() {
        val evento = eventoRepository.obtenerEventoPorId(idEvento)

        if (evento != null) {
            tituloTextView.text = evento.titulo
            descripcionTextView.text = evento.descripcion
            fInicioTextView.text = evento.fInicio
            fVencimientoTextView.text = evento.fVencimiento

            // Obtener el nombre de la etiqueta usando el idEtiqueta
            val nombreEtiqueta = eventoRepository.obtenerNombreEtiquetaPorId(evento.idEtiqueta)
            etiquetaTextView.text = nombreEtiqueta ?: "Etiqueta no encontrada" // Manejar el caso de etiqueta no encontrada

            // Mostrar la ubicación del evento
            ubicacionTextView.text = evento.lugar ?: "Ubicación no encontrada" // Asegúrate de que 'ubicacion' esté en tu modelo de evento
        } else {
            mostrarMensajeError("No se encontró el evento")
        }
    }

    private fun mostrarDialogoConfirmacion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este evento?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarEvento()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarEvento() {
        if (idEvento != -1) {
            try {
                eventoRepository.eliminarEvento(idEvento) // Elimina el evento por su ID
                Toast.makeText(this, "Evento eliminado con éxito", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK) // Establece el resultado OK
                finish() // Cierra la actividad actual y vuelve a la anterior
            } catch (e: Exception) {
                mostrarMensajeError("Error al eliminar el evento")
                e.printStackTrace()
            }
        } else {
            mostrarMensajeError("ID de evento no válido")
        }
    }

    private fun mostrarMensajeError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
