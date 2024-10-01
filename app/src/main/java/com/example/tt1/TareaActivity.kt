package com.example.tt1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.util.Calendar

class TareaActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var et_date: EditText
    private lateinit var et_date2: EditText
    private lateinit var spinnerEtiqueta: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarea) // Asegúrate de que este sea el nombre correcto de tu layout

        // Inicializar los campos
        etTitulo = findViewById(R.id.etTitulo)
        etDescripcion = findViewById(R.id.etDescripcion)
        et_date = findViewById(R.id.et_date)
        et_date2 = findViewById(R.id.et_date2)
        spinnerEtiqueta = findViewById(R.id.spinnerEtiqueta)

        // Inicializar el Spinner con las etiquetas
        val etiquetas = listOf("Trabajo", "Personal", "Urgente", "Otras")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, etiquetas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEtiqueta.adapter = adapter

        // Listener para el Spinner
        spinnerEtiqueta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedLabel = parent.getItemAtPosition(position) as String
                Toast.makeText(this@TareaActivity, "Etiqueta seleccionada: $selectedLabel", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // DatePicker para la fecha de inicio
        et_date.setOnClickListener {
            showDatePicker { date -> et_date.setText(date) }
        }

        // DatePicker para la fecha de vencimiento
        et_date2.setOnClickListener {
            showDatePicker { date -> et_date2.setText(date) }
        }

        // Botón para guardar la tarea
        val btnGuardarTarea: ImageButton = findViewById(R.id.btnGuardarTarea)
        btnGuardarTarea.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val descripcion = etDescripcion.text.toString()
            val fechaInicio = et_date.text.toString()
            val fechaVencimiento = et_date2.text.toString()
            val etiquetaSeleccionada = spinnerEtiqueta.selectedItem.toString()

            if (titulo.isEmpty() || descripcion.isEmpty() || fechaInicio.isEmpty() || fechaVencimiento.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Aquí puedes añadir tu lógica para guardar la tarea
                Toast.makeText(this, "Tarea guardada: $titulo", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Configurar el botón de navegación
        toolbar.setNavigationOnClickListener {
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Inicializar NavigationView
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_calendar -> {
                // Aquí puedes redirigir a la actividad del calendario
                val intent = Intent(this, CalendarActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_task -> {
                // Redirigir a la pantalla de lista de tareas
                val intent = Intent(this, ListaTareasActivity::class.java)
                startActivity(intent)
            }
            // Agregar más casos si tienes otras opciones en el menú
        }

        // Cerrar el menú después de seleccionar un ítem
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "${selectedMonth + 1}-$selectedDay-$selectedYear" // Formato MM-DD-YYYY
            onDateSelected(selectedDate)
        }, year, month, day).show()
    }
}
