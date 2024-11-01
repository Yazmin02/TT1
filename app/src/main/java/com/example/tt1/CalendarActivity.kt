package com.example.tt1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.evento.EventoActivity
import com.example.tt1.evento.EventoCAdapter
import com.example.tt1.evento.ListaEvento
import com.example.tt1.model.entidades.Evento
import com.example.tt1.model.entidades.Tarea
import com.example.tt1.model.repositorios.EventoRepository
import com.example.tt1.model.repositorios.TareaRepository
import com.example.tt1.tarea.ListaActivity
import com.example.tt1.tarea.TareaActivity
import com.example.tt1.tarea.TareaCAdapter
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var addEventButton: Button
    private lateinit var calendarView: CalendarView
    private lateinit var recyclerViewTareas: RecyclerView
    private lateinit var recyclerViewEventos: RecyclerView
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private lateinit var tareaRepository: TareaRepository
    private lateinit var eventoRepository: EventoRepository
    private lateinit var todasLasTareas: List<Tarea>
    private lateinit var todosLosEventos: List<Evento>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        // Inicialización de vistas
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        addEventButton = findViewById(R.id.add_event_button)
        calendarView = findViewById(R.id.calendar_view)
        recyclerViewTareas = findViewById(R.id.recycler_view_tareas)
        recyclerViewEventos = findViewById(R.id.recycler_view_eventos)

        setSupportActionBar(toolbar)

        // Configura el botón de menú para abrir/cerrar el DrawerLayout
        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Configura el botón de agregar para mostrar el PopupMenu
        addEventButton.setOnClickListener {
            showPopupMenu(it)
        }

        // Configura el NavigationView
        navigationView.setNavigationItemSelectedListener { item ->
            onNavigationItemSelected(item)
        }

        // Inicializar repositorios y cargar todas las tareas y eventos
        tareaRepository = TareaRepository(DatabaseHelper(this))
        eventoRepository = EventoRepository(DatabaseHelper(this))
        todasLasTareas = tareaRepository.obtenerTareas()
        todosLosEventos = eventoRepository.obtenerEvento()

        // Listeners para la selección de día en el calendario
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time
            loadTasksAndEvents(selectedDate)
        }

        // Enlaces de selección de vista
        findViewById<TextView>(R.id.view_selector_semana).setOnClickListener {
            val intent = Intent(this, SemanaActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.view_selector_dia).setOnClickListener {
            val intent = Intent(this, DiaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadTasksAndEvents(date: Date) {
        val tareas = obtenerTareasPorFecha(date)
        val tareaAdapter = TareaCAdapter(tareas)
        recyclerViewTareas.adapter = tareaAdapter
        recyclerViewTareas.layoutManager = LinearLayoutManager(this)

        val eventos = obtenerEventosPorFecha(date)
        val eventoAdapter = EventoCAdapter(eventos)
        recyclerViewEventos.adapter = eventoAdapter
        recyclerViewEventos.layoutManager = LinearLayoutManager(this)
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_add_task -> {
                    val intent = Intent(this, TareaActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_add_event -> {
                    val intent = Intent(this, EventoActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun obtenerTareasPorFecha(fecha: Date): List<Tarea> {
        val calendarFecha = Calendar.getInstance().apply { time = fecha }
        return todasLasTareas.filter { tarea ->
            val tareaFecha = dateFormat.parse(tarea.fVencimiento) // Convierte fVencimiento a Date
            if (tareaFecha != null) {
                val calendarTarea = Calendar.getInstance().apply { time = tareaFecha }
                calendarTarea.get(Calendar.YEAR) == calendarFecha.get(Calendar.YEAR) &&
                        calendarTarea.get(Calendar.MONTH) == calendarFecha.get(Calendar.MONTH) &&
                        calendarTarea.get(Calendar.DAY_OF_MONTH) == calendarFecha.get(Calendar.DAY_OF_MONTH) &&
                        tarea.estado == 0
            } else {
                false
            }
        }
    }

    private fun obtenerEventosPorFecha(fecha: Date): List<Evento> {
        val calendarFecha = Calendar.getInstance().apply { time = fecha }
        return todosLosEventos.filter { evento ->
            val eventoFecha = dateFormat.parse(evento.fVencimiento) // Convierte fVencimiento a Date
            if (eventoFecha != null) {
                val calendarEvento = Calendar.getInstance().apply { time = eventoFecha }
                calendarEvento.get(Calendar.YEAR) == calendarFecha.get(Calendar.YEAR) &&
                        calendarEvento.get(Calendar.MONTH) == calendarFecha.get(Calendar.MONTH) &&
                        calendarEvento.get(Calendar.DAY_OF_MONTH) == calendarFecha.get(Calendar.DAY_OF_MONTH) &&
                        evento.estado == 0
            } else {
                false
            }
        }
    }


    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this, PrincipalActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_task -> {
                val intent = Intent(this, ListaActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_evento -> {
                val intent = Intent(this, ListaEvento::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
