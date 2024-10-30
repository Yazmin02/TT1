package com.example.tt1.tarea

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tt1.CalendarActivity
import com.example.tt1.DatabaseHelper
import com.example.tt1.PrincipalActivity
import com.example.tt1.R
import com.example.tt1.ReminderReceiver
import com.example.tt1.evento.ListaEvento
import com.example.tt1.model.entidades.Tarea
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
    private lateinit var cbRecordatorio: CheckBox
    private lateinit var spinnerRecordatorio: Spinner
    private lateinit var timeLabel: TextView
    private lateinit var btnGuardarTarea: ImageButton
    private lateinit var tareaDao: TareaDao

    // Variables para almacenar fechas y horas seleccionadas
    private var fechaInicioCalendar: Calendar = Calendar.getInstance()
    private var fechaVencimientoCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarea)

        createNotificationChannel()

        // Inicializar las vistas
        etTitulo = findViewById(R.id.etTitulo)
        etDescripcion = findViewById(R.id.etDescripcion)
        etDateInicio = findViewById(R.id.et_date)
        etDateVencimiento = findViewById(R.id.et_date2)
        spinnerEtiqueta = findViewById(R.id.spinnerEtiqueta)
        cbRecordatorio = findViewById(R.id.cbRecordatorio)
        spinnerRecordatorio = findViewById(R.id.spinnerRecordatorio)
        timeLabel = findViewById(R.id.timeLabel)
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea)

        // Inicializar el Spinner con las etiquetas
        val etiquetas = listOf("Escuela", "Trabajo", "Ocio")
        val adapterEtiqueta = ArrayAdapter(this, android.R.layout.simple_spinner_item, etiquetas)
        adapterEtiqueta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEtiqueta.adapter = adapterEtiqueta

        // Listener para el Spinner de etiquetas
        spinnerEtiqueta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLabel = parent.getItemAtPosition(position) as String
                Toast.makeText(this@TareaActivity, "Etiqueta seleccionada: $selectedLabel", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Configurar las opciones del Spinner de recordatorio
        val opcionesRecordatorio = listOf("1 día antes", "1 hora antes", "15 minutos antes")
        val adapterRecordatorio = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesRecordatorio)
        adapterRecordatorio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRecordatorio.adapter = adapterRecordatorio

        // Mostrar el Spinner solo si el CheckBox está marcado
        cbRecordatorio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                spinnerRecordatorio.visibility = View.VISIBLE
                timeLabel.visibility = View.VISIBLE
            } else {
                spinnerRecordatorio.visibility = View.GONE
                timeLabel.visibility = View.GONE
            }
        }

        setupDateTimePickers()

        tareaDao = TareaDao(DatabaseHelper(this).writableDatabase)

        btnGuardarTarea.setOnClickListener {
            guardarTarea()
        }

        setupToolbar()

        // Inicializar NavigationView
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
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

    private fun setupDateTimePickers() {
        etDateInicio.setOnClickListener {
            showDateTimePicker { calendar ->
                fechaInicioCalendar = calendar
                val formattedDateTime = formatCalendar(calendar)
                etDateInicio.setText(formattedDateTime)
            }
        }

        etDateVencimiento.setOnClickListener {
            showDateTimePicker { calendar ->
                fechaVencimientoCalendar = calendar
                val formattedDateTime = formatCalendar(calendar)
                etDateVencimiento.setText(formattedDateTime)
            }
        }
    }

    private fun showDateTimePicker(onDateTimeSelected: (Calendar) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Actualizar el calendario con la fecha seleccionada
            calendar.set(Calendar.YEAR, selectedYear)
            calendar.set(Calendar.MONTH, selectedMonth)
            calendar.set(Calendar.DAY_OF_MONTH, selectedDay)

            // Mostrar el TimePickerDialog después de seleccionar la fecha
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                onDateTimeSelected(calendar)
            }, hour, minute, true).show()

        }, year, month, day).show()
    }

    private fun formatCalendar(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun guardarTarea() {
        val titulo = etTitulo.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val fechaInicio = etDateInicio.text.toString().trim()
        val fechaVencimiento = etDateVencimiento.text.toString().trim()
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

        // Validar que la fecha de vencimiento no sea anterior a la fecha de inicio
        if (fechaVencimientoCalendar.before(fechaInicioCalendar)) {
            Toast.makeText(
                this,
                "La fecha y hora de vencimiento no pueden ser anteriores a la fecha y hora de inicio.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val tarea = Tarea(
            id = 0, // Asume que la base de datos asigna el ID automáticamente
            titulo = titulo,
            descripcion = descripcion,
            fInicio = formatCalendar(fechaInicioCalendar),
            fVencimiento = formatCalendar(fechaVencimientoCalendar),
            estado = 0,
            idEtiqueta = idEtiqueta,
            idUsuario = 1
        )

        try {
            tareaDao.insertarTarea(tarea)
            Toast.makeText(this, "Tarea guardada: $titulo", Toast.LENGTH_SHORT).show()

            // Programar el recordatorio si está activado
            if (cbRecordatorio.isChecked) {
                val reminderTime = fechaVencimientoCalendar.timeInMillis
                val tiempoSeleccionado = when (spinnerRecordatorio.selectedItem.toString()) {
                    "1 día antes" -> 24 * 60 * 60 * 1000L // 1 día en milisegundos
                    "1 hora antes" -> 60 * 60 * 1000L // 1 hora en milisegundos
                    "15 minutos antes" -> 15 * 60 * 1000L // 15 minutos en milisegundos
                    else -> 0L
                }

                // Calcular el tiempo exacto para el recordatorio
                val tiempoRecordatorio = reminderTime - tiempoSeleccionado

                // Verificar que el tiempo de recordatorio no sea en el pasado
                if (tiempoRecordatorio > System.currentTimeMillis()) {
                    val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    val fechaEntrega = formatoFecha.format(fechaVencimientoCalendar.time)

                    setReminder(
                        titulo,
                        "Recordatorio: Debes entregar '$titulo' el $fechaEntrega",
                        tiempoRecordatorio
                    )
                } else {
                    Toast.makeText(
                        this,
                        "El tiempo de recordatorio seleccionado ya pasó.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al guardar la tarea: ${e.message}", Toast.LENGTH_SHORT)
                .show()
            Log.e("TareaActivity", "Error al guardar tarea: ${e.message}")
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
            title.hashCode(), // Usar un hash único basado en el título para diferenciar alarmas
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
            R.id.nav_home -> {
                startActivity(Intent(this, PrincipalActivity::class.java))
            }
            R.id.nav_evento -> {
                // Redirige a la actividad de lista de eventos
                val intent = Intent(this, ListaEvento::class.java)
                startActivity(intent)
            }
            R.id.nav_evento -> {
                // Redirige a la actividad de lista de tareas
                val intent = Intent(this, ListaEvento::class.java)
                startActivity(intent)
            }
        }
        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        return true
    }
}