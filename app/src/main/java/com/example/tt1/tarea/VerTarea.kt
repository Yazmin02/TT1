package com.example.tt1.tarea

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.DatabaseHelper
import com.example.tt1.R
import com.example.tt1.model.repositorios.TareaRepository

class VerTareaActivity : AppCompatActivity() {

    private lateinit var tituloTextView: TextView
    private lateinit var descripcionTextView: TextView
    private lateinit var fInicioTextView: TextView
    private lateinit var fVencimientoTextView: TextView
    private lateinit var etiquetaTextView: TextView
    private lateinit var botonEditar: Button
    private lateinit var botonEliminar: Button
    private lateinit var tareaRepository: TareaRepository
    private var tareaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ver_tarea)

        inicializarVistas()
        tareaRepository = TareaRepository(DatabaseHelper(this))

        // Obtener el ID de la tarea del Intent
        tareaId = intent.getIntExtra("idTarea", -1)

        // Cargar los detalles de la tarea
        cargarDetallesTarea()

        configurarEventos()
    }

    override fun onResume() {
        super.onResume()
        // Actualizar los detalles de la tarea cada vez que la actividad se reanuda
        cargarDetallesTarea()
    }

    private fun inicializarVistas() {
        tituloTextView = findViewById(R.id.tituloTextView)
        descripcionTextView = findViewById(R.id.descripcionTextView)
        fInicioTextView = findViewById(R.id.fInicioTextView)
        fVencimientoTextView = findViewById(R.id.fVencimientoTextView)
        etiquetaTextView = findViewById(R.id.etiquetaTextView)
        botonEditar = findViewById(R.id.botonEditar)
        botonEliminar = findViewById(R.id.botonEliminar)
    }

    private fun configurarEventos() {
        botonEditar.setOnClickListener {
            if (tareaId != -1) {
                val intent = Intent(this, EditarTareaActivity::class.java).apply {
                    putExtra("idTarea", tareaId)
                }
                startActivity(intent)
            } else {
                mostrarMensajeError("ID de tarea no válido")
            }
        }

        botonEliminar.setOnClickListener {
            mostrarDialogoConfirmacion()
        }
    }

    private fun cargarDetallesTarea() {
        val tarea = tareaRepository.obtenerTareaPorId(tareaId)

        if (tarea != null) {
            tituloTextView.text = tarea.titulo
            descripcionTextView.text = tarea.descripcion
            fInicioTextView.text = tarea.fInicio
            fVencimientoTextView.text = tarea.fVencimiento
            val nombreEtiqueta = tareaRepository.obtenerNombreEtiquetaPorId(tarea.idEtiqueta)
            etiquetaTextView.text = nombreEtiqueta ?: "Etiqueta no encontrada" // Manejar el caso de etiqueta no encontrada
        } else {
            mostrarMensajeError("No se encontró la tarea")
        }
    }

    private fun mostrarDialogoConfirmacion() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarTarea()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarTarea() {
        if (tareaId != -1) {
            try {
                tareaRepository.eliminarTarea(tareaId) // Elimina la tarea por su ID
                Toast.makeText(this, "Tarea eliminada con éxito", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK) // Establece el resultado OK
                finish() // Cierra la actividad actual y vuelve a la anterior
            } catch (e: Exception) {
                mostrarMensajeError("Error al eliminar la tarea")
                e.printStackTrace()
            }
        } else {
            mostrarMensajeError("ID de tarea no válido")
        }
    }


    private fun mostrarMensajeError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
