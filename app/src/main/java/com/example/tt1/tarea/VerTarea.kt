package com.example.tt1.tarea

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.DatabaseHelper
import com.example.tt1.R
import com.example.tt1.RecompensasActivity
import com.example.tt1.model.RecompensaModel
import com.example.tt1.model.repositorios.TareaRepository
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class VerTareaActivity : AppCompatActivity() {

    private lateinit var tituloTextView: TextView
    private lateinit var descripcionTextView: TextView
    private lateinit var fInicioTextView: TextView
    private lateinit var fVencimientoTextView: TextView
    private lateinit var etiquetaTextView: TextView
    private lateinit var botonEditar: Button
    private lateinit var botonEliminar: Button
    private lateinit var botonCompletar: Button // Nuevo botón
    private lateinit var tareaRepository: TareaRepository
    private var tareaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ver_tarea)

        inicializarVistas()
        tareaRepository = TareaRepository(DatabaseHelper(this))
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
        botonCompletar = findViewById(R.id.botonCompletar) // Inicializa el nuevo botón
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

        botonCompletar.setOnClickListener {
            marcarTareaComoCompletada()
        }
    }

    private fun marcarTareaComoCompletada() {
        if (tareaId != -1) {
            try {
                // Marca la tarea como completa en la base de datos
                tareaRepository.marcarTareaComoCompleta(tareaId)

                // Obtén el ID del usuario (actualiza según tu implementación)
                val idUsuario = 1

                // Obtén la fecha límite de la tarea y la fecha de completado
                val tarea = tareaRepository.obtenerTareaPorId(tareaId)
                if (tarea != null) {
                    val fechaLimite = tarea.fVencimiento.toDateInMillis()
                    val fechaCompletada = System.currentTimeMillis()

                    // Asigna puntos basados en la diferencia de fechas
                    val recompensaModel = RecompensaModel(this)
                    recompensaModel.asignarPuntosPorTarea(fechaLimite, fechaCompletada, idUsuario)

                    // Verifica logros después de completar la tarea
                    recompensaModel.verificarLogros(idUsuario)

                    Toast.makeText(this, "Tarea completada. ¡Puntos asignados!", Toast.LENGTH_SHORT).show()

                    // Redirige a RecompensasActivity para ver los puntos y logros actualizados
                    val intent = Intent(this, RecompensasActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    mostrarMensajeError("Error al obtener detalles de la tarea")
                }
            } catch (e: Exception) {
                mostrarMensajeError("Error al marcar la tarea como completada")
                e.printStackTrace()
            }
        } else {
            mostrarMensajeError("ID de tarea no válido")
        }
    }



    private fun String.toDateInMillis(): Long {
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            formatoFecha.parse(this)?.time ?: System.currentTimeMillis()
        } catch (e: ParseException) {
            System.currentTimeMillis()
        }
    }


    private fun cargarDetallesTarea() {
        val tarea = tareaRepository.obtenerTareaPorId(tareaId)
        if (tarea != null) {
            tituloTextView.text = tarea.titulo
            descripcionTextView.text = tarea.descripcion
            fInicioTextView.text = tarea.fInicio
            fVencimientoTextView.text = tarea.fVencimiento
            etiquetaTextView.text = tareaRepository.obtenerNombreEtiquetaPorId(tarea.idEtiqueta).toString()

            // Marcar el texto como completado si la tarea está completada
            if (tarea.estado == 1) { // Asumiendo que 1 es el estado completado
                marcarTareaComoCompletadaVisualmente()
                botonCompletar.isEnabled = false // Deshabilitar el botón si ya está completada
            }
        } else {
            mostrarMensajeError("Tarea no encontrada")
        }
    }

    private fun marcarTareaComoCompletadaVisualmente() {
        tituloTextView.paintFlags = tituloTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        descripcionTextView.paintFlags = descripcionTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun mostrarDialogoConfirmacion() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Tarea")
            .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
            .setPositiveButton("Eliminar") { _, _ -> eliminarTarea() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarTarea() {
        Log.d("VerTareaActivity", "Eliminando tarea con ID: $tareaId") // Log para verificar el ID
        try {
            tareaRepository.eliminarTarea(tareaId)
            Toast.makeText(this, "Tarea eliminada con éxito", Toast.LENGTH_SHORT).show()
            finish() // Regresar a la lista de tareas
        } catch (e: Exception) {
            mostrarMensajeError("Error al eliminar la tarea: ${e.message}")
        }
    }


    private fun mostrarMensajeError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
