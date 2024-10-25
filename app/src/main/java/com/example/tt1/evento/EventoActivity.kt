package com.example.tt1.evento

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tt1.CalendarActivity
import com.example.tt1.DatabaseHelper
import com.example.tt1.MapActivity
import com.example.tt1.PrincipalActivity
import com.example.tt1.R
import com.example.tt1.ReminderReceiver
import com.example.tt1.model.entidades.Evento
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EventoActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val LOCATION_REQUEST_CODE = 1

    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etDateInicio: EditText
    private lateinit var etDateVencimiento: EditText
    private lateinit var spinnerEtiqueta: Spinner
    private lateinit var cbRecordatorio: CheckBox
    private lateinit var spinnerRecordatorio: Spinner
    private lateinit var timeLabel: TextView
    private lateinit var btnGuardarEvento: ImageButton
    private lateinit var etUbicacion: EditText
    private lateinit var eventoDao: EventoDao
    private lateinit var btnSeleccionar: ImageButton

    // Variables para almacenar fechas y horas seleccionadas
    private var fechaInicioCalendar: Calendar = Calendar.getInstance()
    private var fechaVencimientoCalendar: Calendar = Calendar.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evento)

        createNotificationChannel()
        initializeViews()
        setupSpinners()
        setupDateTimePickers()
        setupEventListeners()

        eventoDao = EventoDao(DatabaseHelper(this).writableDatabase)
        checkNotificationPermissions()
    }

    private fun initializeViews() {
        etTitulo = findViewById(R.id.etTitulo)
        etDescripcion = findViewById(R.id.etDescripcion)
        etDateInicio = findViewById(R.id.et_date)
        etDateVencimiento = findViewById(R.id.et_date2)
        spinnerEtiqueta = findViewById(R.id.spinnerEtiqueta)
        cbRecordatorio = findViewById(R.id.cbRecordatorio)
        spinnerRecordatorio = findViewById(R.id.spinnerRecordatorio)
        timeLabel = findViewById(R.id.timeLabel)
        btnGuardarEvento = findViewById(R.id.btnGuardarEvento)
        etUbicacion = findViewById(R.id.etUbicacion)
        btnSeleccionar = findViewById(R.id.btnSeleccionar)
    }

    private fun setupSpinners() {
        // Inicializar el Spinner con las etiquetas
        val etiquetas = listOf("Escuela", "Trabajo", "Ocio")
        val adapterEtiqueta = ArrayAdapter(this, android.R.layout.simple_spinner_item, etiquetas)
        adapterEtiqueta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEtiqueta.adapter = adapterEtiqueta

        // Listener para el Spinner de etiquetas
        spinnerEtiqueta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLabel = parent.getItemAtPosition(position) as String
                Toast.makeText(this@EventoActivity, "Etiqueta seleccionada: $selectedLabel", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Configurar las opciones del Spinner de recordatorio
        val opcionesRecordatorio = listOf("1 día antes", "1 hora antes", "15 minutos antes")
        val adapterRecordatorio = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesRecordatorio)
        adapterRecordatorio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRecordatorio.adapter = adapterRecordatorio
    }

    private fun setupDateTimePickers() {
        etDateInicio.setOnClickListener {
            showDateTimePicker { calendar ->
                fechaInicioCalendar = calendar
                etDateInicio.setText(formatCalendar(calendar))
            }
        }

        etDateVencimiento.setOnClickListener {
            showDateTimePicker { calendar ->
                fechaVencimientoCalendar = calendar
                etDateVencimiento.setText(formatCalendar(calendar))
            }
        }
    }

    private fun showDateTimePicker(onDateTimeSelected: (Calendar) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            calendar.set(year, month, day)
            TimePickerDialog(this, { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                onDateTimeSelected(calendar)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun formatCalendar(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun setupEventListeners() {
        btnSeleccionar.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivityForResult(intent, LOCATION_REQUEST_CODE)
        }

        cbRecordatorio.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                spinnerRecordatorio.visibility = View.VISIBLE
                timeLabel.visibility = View.VISIBLE
            } else {
                spinnerRecordatorio.visibility = View.GONE
                timeLabel.visibility = View.GONE
            }
        }

        btnGuardarEvento.setOnClickListener {
            guardarEvento()
        }

        // Inicializar NavigationView
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun checkNotificationPermissions() {
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
                description = "Canal para recordatorios de eventos"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun guardarEvento() {
        val titulo = etTitulo.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val fechaInicio = etDateInicio.text.toString().trim()
        val fechaVencimiento = etDateVencimiento.text.toString().trim()
        val etiquetaSeleccionada = spinnerEtiqueta.selectedItem.toString()
        val lugar = etUbicacion.text.toString().trim()

        val idEtiqueta = when (etiquetaSeleccionada) {
            "Escuela" -> 1
            "Trabajo" -> 2
            "Ocio" -> 3
            else -> null
        }

        if (titulo.isEmpty() || descripcion.isEmpty() || fechaInicio.isEmpty() || fechaVencimiento.isEmpty() || idEtiqueta == null) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (fechaVencimientoCalendar.before(fechaInicioCalendar)) {
            Toast.makeText(this, "La fecha y hora de vencimiento no pueden ser anteriores a la fecha y hora de inicio.", Toast.LENGTH_SHORT).show()
            return
        }

        val evento = Evento(
            idEvento = 0,
            titulo = titulo,
            descripcion = descripcion,
            fInicio = formatCalendar(fechaInicioCalendar),
            fVencimiento = formatCalendar(fechaVencimientoCalendar),
            idEtiqueta = idEtiqueta,
            idUsuario = 1,
            lugar = lugar
        )

        try {
            eventoDao.insertarEvento(evento)
            Toast.makeText(this, "Evento guardado: $titulo", Toast.LENGTH_SHORT).show()
            setupReminder(titulo)

            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al guardar el evento.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupReminder(titulo: String) {
        if (cbRecordatorio.isChecked) {
            val selectedReminder = spinnerRecordatorio.selectedItem.toString()
            val reminderTime = when (selectedReminder) {
                "1 día antes" -> 24 * 60 * 60 * 1000 // 1 día
                "1 hora antes" -> 60 * 60 * 1000 // 1 hora
                "15 minutos antes" -> 15 * 60 * 1000 // 15 minutos
                else -> 0
            }

            val alarmTime = fechaInicioCalendar.timeInMillis - reminderTime
            val intent = Intent(this, ReminderReceiver::class.java).apply {
                putExtra("titulo", titulo)
            }

            val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val alarmManager = getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                startActivity(Intent(this, PrincipalActivity::class.java))
            }
            R.id.nav_calendar -> {
                startActivity(Intent(this, CalendarActivity::class.java))
            }
            // Otras opciones del menú
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val latLng = data?.getParcelableExtra<LatLng>("latLng")
            latLng?.let {
                etUbicacion.setText("Lat: ${it.latitude}, Lng: ${it.longitude}")
            }
        }
    }

}
