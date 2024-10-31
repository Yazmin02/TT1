package com.example.tt1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
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

class DiaActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var addEventButton: Button
    private lateinit var dayTitle: TextView
    private lateinit var recyclerViewTareas: RecyclerView
    private lateinit var recyclerViewEventos: RecyclerView
    private val calendar: Calendar = Calendar.getInstance()

    private lateinit var tareaRepository: TareaRepository
    private lateinit var eventoRepository: EventoRepository
    private lateinit var todasLasTareas: List<Tarea>
    private lateinit var todosLosEventos: List<Evento>
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dia)

        // Inicialización de vistas
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        addEventButton = findViewById(R.id.add_event_button)
        dayTitle = findViewById(R.id.day_title)
        recyclerViewTareas = findViewById(R.id.recycler_view_tareas)
        recyclerViewEventos = findViewById(R.id.recycler_view_eventos)

        // Configuración del toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Configurar el botón de agregar para mostrar el PopupMenu
        addEventButton.setOnClickListener {
            showPopupMenu(it)
        }

        // En el método onCreate() de CalendarActivity:
        findViewById<TextView>(R.id.view_selector_semana).setOnClickListener {
            val intent = Intent(this, SemanaActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.view_selector_mes).setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
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

        // Configura la navegación entre días
        findViewById<TextView>(R.id.previous_day).setOnClickListener {
            navigateDay(-1)
        }
        findViewById<TextView>(R.id.next_day).setOnClickListener {
            navigateDay(1)
        }

        // Cargar el día actual al iniciar la actividad
        loadDay()
    }

    private fun navigateDay(offset: Int) {
        calendar.add(Calendar.DAY_OF_YEAR, offset)
        loadDay()
    }

    private fun loadDay() {
        val dateFormat = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault())
        dayTitle.text = dateFormat.format(calendar.time)

        // Filtrar y cargar tareas y eventos para el día actual
        val tareas = obtenerTareasPorFecha(calendar.time)
        val tareaDiaAdapter = TareaCAdapter(tareas)
        recyclerViewTareas.adapter = tareaDiaAdapter
        recyclerViewTareas.layoutManager = LinearLayoutManager(this)

        val eventos = obtenerEventosPorFecha(calendar.time)
        val eventoDiaAdapter = EventoCAdapter(eventos)
        recyclerViewEventos.adapter = eventoDiaAdapter
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
            val tareaFecha = dateFormat.parse(tarea.fVencimiento)
            if (tareaFecha != null) {
                val calendarTarea = Calendar.getInstance().apply { time = tareaFecha }
                calendarTarea.get(Calendar.YEAR) == calendarFecha.get(Calendar.YEAR) &&
                        calendarTarea.get(Calendar.DAY_OF_YEAR) == calendarFecha.get(Calendar.DAY_OF_YEAR) &&
                        tarea.estado == 0
            } else {
                false
            }
        }
    }

    private fun obtenerEventosPorFecha(fecha: Date): List<Evento> {
        val calendarFecha = Calendar.getInstance().apply { time = fecha }
        return todosLosEventos.filter { evento ->
            val eventoFecha = dateFormat.parse(evento.fVencimiento)
            if (eventoFecha != null) {
                val calendarEvento = Calendar.getInstance().apply { time = eventoFecha }
                calendarEvento.get(Calendar.YEAR) == calendarFecha.get(Calendar.YEAR) &&
                        calendarEvento.get(Calendar.DAY_OF_YEAR) == calendarFecha.get(Calendar.DAY_OF_YEAR) &&
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
