package com.example.tt1.evento

import android.annotation.SuppressLint
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
import com.example.tt1.model.entidades.Evento
import com.example.tt1.model.repositorios.EventoRepository
import com.example.tt1.tarea.ListaActivity
import com.google.android.material.navigation.NavigationView
import java.util.Calendar

class EditarEvento : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etFechaVencimiento: EditText
    private lateinit var btnGuardarEvento: ImageButton
    private lateinit var evento: Evento
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var eventoRepository: EventoRepository

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editar_evento)

        // Inicializar campos
        etTitulo = findViewById(R.id.etTitulo)
        etDescripcion = findViewById(R.id.etDescripcion)
        etFechaVencimiento = findViewById(R.id.et_date2)
        btnGuardarEvento = findViewById(R.id.btnGuardarEvento)

        // Obtener el ID del evento desde el Intent
        val idEvento = intent.getIntExtra("idEvento", -1)
        if (idEvento == -1) {
            Toast.makeText(this, "ID de evento no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Inicializar el repositorio de eventos
        eventoRepository = EventoRepository(DatabaseHelper(this))

        // Cargar el evento usando el ID
        evento = eventoRepository.obtenerEventoPorId(idEvento)!!
        if (evento != null) {
            cargarDatosEvento()
        } else {
            Toast.makeText(this, "No se encontró el evento", Toast.LENGTH_LONG).show()
            finish()
        }

        // Configurar el botón de guardar
        btnGuardarEvento.setOnClickListener {
            actualizarEvento()
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

    private fun cargarDatosEvento() {
        etTitulo.setText(evento.titulo)
        etDescripcion.setText(evento.descripcion)
        etFechaVencimiento.setText(evento.fVencimiento)
    }

    private fun actualizarEvento() {
        try {
            if (etTitulo.text.isEmpty() || etFechaVencimiento.text.isEmpty()) {
                Toast.makeText(this, "Título y Fecha de Vencimiento son obligatorios", Toast.LENGTH_SHORT).show()
                return
            }

            evento.titulo = etTitulo.text.toString()
            evento.descripcion = etDescripcion.text.toString()
            evento.fVencimiento = etFechaVencimiento.text.toString()

            eventoRepository.actualizarEvento(evento)
            Toast.makeText(this, "Evento actualizado", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al actualizar el evento: ${e.message}", Toast.LENGTH_SHORT).show()
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
