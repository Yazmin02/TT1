package com.example.tt1

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.tt1.model.Tarea
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TareaActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etDateInicio: EditText
    private lateinit var etDateVencimiento: EditText
    private lateinit var spinnerEtiqueta: Spinner
    private lateinit var tareaDao: TareaDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarea)

        createNotificationChannel()

        // Inicializar los campos
        etTitulo = findViewById(R.id.etTitulo)
        etDescripcion = findViewById(R.id.etDescripcion)
        etDateInicio = findViewById(R.id.et_date)
        etDateVencimiento = findViewById(R.id.et_date2)
        spinnerEtiqueta = findViewById(R.id.spinnerEtiqueta)

        // Inicializar el Spinner con las etiquetas
        val etiquetas = listOf("Escuela", "Trabajo", "Ocio")
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

        setupDatePickers()

        tareaDao = TareaDao(DatabaseHelper(this).writableDatabase)

        findViewById<ImageButton>(R.id.btnGuardarTarea).setOnClickListener {
            guardarTarea()
        }

        setupToolbar()

        // Inicializar NavigationView
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Recordatorios",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para recordatorios de tareas"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setupDatePickers() {
        etDateInicio.setOnClickListener {
            showDatePicker { date -> etDateInicio.setText(date) }
        }

        etDateVencimiento.setOnClickListener {
            showDatePicker { date -> etDateVencimiento.setText(date) }
        }
    }

    private fun guardarTarea() {
        val titulo = etTitulo.text.toString()
        val descripcion = etDescripcion.text.toString()
        val fechaInicio = etDateInicio.text.toString()
        val fechaVencimiento = etDateVencimiento.text.toString()
        val etiquetaSeleccionada = spinnerEtiqueta.selectedItem.toString()

        // Obtener el ID de la etiqueta seleccionada
        val idEtiqueta = when (etiquetaSeleccionada) {
            "Escuela" -> 1
            "Trabajo" -> 2
            "Ocio" -> 3
            else -> null
        }

        // Validar los campos
        if (titulo.isEmpty() || descripcion.isEmpty() || fechaInicio.isEmpty() || fechaVencimiento.isEmpty() || idEtiqueta == null) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return // Salimos si hay campos vacíos
        }

        val tarea = Tarea(0, titulo, descripcion, fechaInicio, fechaVencimiento, idEtiqueta, idUsuario = 1)

        try {
            tareaDao.insertarTarea(tarea)
            Toast.makeText(this, "Tarea guardada: $titulo", Toast.LENGTH_SHORT).show()

            // Programar el recordatorio
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = dateFormat.parse(fechaVencimiento)
            val reminderTime = date?.time ?: System.currentTimeMillis()
            setReminder(titulo, "Recordatorio para la tarea: $titulo", reminderTime)

            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al guardar la tarea: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("TareaActivity", "Error: ${e.message}")
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setReminder(title: String, message: String, reminderTime: Long) {
        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_calendar -> {
                startActivity(Intent(this, CalendarActivity::class.java))
            }
            R.id.nav_task -> {
                startActivity(Intent(this, ListaTareasActivity::class.java))
            }
        }
        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        return true
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            onDateSelected(selectedDate)
        }, year, month, day).show()
    }
}
