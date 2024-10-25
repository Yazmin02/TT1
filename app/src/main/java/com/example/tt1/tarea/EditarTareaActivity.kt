package com.example.tt1.tarea

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tt1.CalendarActivity
import com.example.tt1.DatabaseHelper
import com.example.tt1.PrincipalActivity
import com.example.tt1.R
import com.example.tt1.model.entidades.Tarea
import com.example.tt1.model.repositorios.TareaRepository
import com.google.android.material.navigation.NavigationView
import java.util.Calendar

class EditarTareaActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etFechaVencimiento: EditText
    private lateinit var btnGuardarTarea: ImageButton
    private lateinit var tarea: Tarea
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var tareaRepository: TareaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editar_tarea)

        // Inicializar campos
        etTitulo = findViewById(R.id.etTitulo)
        etDescripcion = findViewById(R.id.etDescripcion)
        etFechaVencimiento = findViewById(R.id.et_date2)
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea)

        // Obtener el ID de la tarea desde el Intent
        val tareaId = intent.getIntExtra("idTarea", -1)
        if (tareaId == -1) {
            Toast.makeText(this, "ID de tarea no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Inicializar el repositorio de tareas
        tareaRepository = TareaRepository(DatabaseHelper(this))

        // Cargar la tarea usando el ID
        tarea = tareaRepository.obtenerTareaPorId(tareaId)!!
        if (tarea != null) {
            cargarDatosTarea()
        } else {
            Toast.makeText(this, "No se encontró la tarea", Toast.LENGTH_LONG).show()
            finish()
        }

        // Configurar el botón de guardar
        btnGuardarTarea.setOnClickListener {
            actualizarTarea()
        }

        // Configurar la selección de fecha
        etFechaVencimiento.setOnClickListener {
            mostrarSelectorFecha()
        }

        // Configuración del DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)
    }

    private fun cargarDatosTarea() {
        etTitulo.setText(tarea.titulo)
        etDescripcion.setText(tarea.descripcion)
        etFechaVencimiento.setText(tarea.fVencimiento)
    }

    private fun actualizarTarea() {
        try {
            if (etTitulo.text.isEmpty() || etFechaVencimiento.text.isEmpty()) {
                Toast.makeText(this, "Título y Fecha de Vencimiento son obligatorios", Toast.LENGTH_SHORT).show()
                return
            }

            tarea.titulo = etTitulo.text.toString()
            tarea.descripcion = etDescripcion.text.toString()
            tarea.fVencimiento = etFechaVencimiento.text.toString()

            tareaRepository.actualizarTarea(tarea)
            Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al actualizar la tarea: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarSelectorFecha() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fechaSeleccionada = "$dayOfMonth/${month + 1}/$year"
                etFechaVencimiento.setText(fechaSeleccionada)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_calendar -> startActivity(Intent(this, CalendarActivity::class.java))
            R.id.nav_task -> startActivity(Intent(this, ListaActivity::class.java))
            R.id.nav_home -> startActivity(Intent(this, PrincipalActivity::class.java))
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
