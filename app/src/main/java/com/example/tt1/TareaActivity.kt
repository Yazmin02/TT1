package com.example.tt1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat

class TareaActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var timePicker: TimePicker
    private lateinit var etDate: EditText
    private lateinit var spinnerEtiqueta: Spinner
    private lateinit var btnGuardarTarea: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarea)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        etTitulo = findViewById(R.id.etTitulo)
        etDescripcion = findViewById(R.id.etDescripcion)
        etDate = findViewById(R.id.et_date)
        spinnerEtiqueta = findViewById(R.id.spinnerEtiqueta)
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea)

        setSupportActionBar(toolbar)

        // Configura el botón de menú para abrir/cerrar el DrawerLayout
        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Configura el NavigationView
        navigationView.setNavigationItemSelectedListener { item ->
            onNavigationItemSelected(item)
        }

        // Configura el botón para guardar la tarea
        btnGuardarTarea.setOnClickListener {
            saveTask()
        }
    }

    private fun saveTask() {
        val title = etTitulo.text.toString()
        val date = etDate.text.toString()
        val hour = timePicker.hour
        val minute = timePicker.minute
        val label = spinnerEtiqueta.selectedItem.toString()

        // Verifica las restricciones
        if (title.isEmpty()) {
            Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }

        if (date.isEmpty()) {
            Toast.makeText(this, "La fecha es obligatoria", Toast.LENGTH_SHORT).show()
            return
        }

        if (hour == 0 && minute == 0) {
            Toast.makeText(this, "La hora es obligatoria", Toast.LENGTH_SHORT).show()
            return
        }

        // Restricción para la etiqueta
        if (label.isEmpty()) {
            Toast.makeText(this, "Por favor, selecciona una etiqueta", Toast.LENGTH_SHORT).show()
            return
        }

        // Aquí puedes proceder a guardar la tarea
        Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show()
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Redirige a la actividad principal
                val intent = Intent(this, PrincipalActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_calendar -> {
                // Redirige a la pantalla de calendario
                val intent = Intent(this, CalendarActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
