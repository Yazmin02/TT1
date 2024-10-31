package com.example.tt1

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

class SemanaActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var addEventButton: Button
    private lateinit var weekTitle: TextView

    // RecyclerView para cada día de la semana (tareas y eventos)
    private lateinit var recyclerViewTareasLunes: RecyclerView
    private lateinit var recyclerViewEventosLunes: RecyclerView
    private lateinit var recyclerViewTareasMartes: RecyclerView
    private lateinit var recyclerViewEventosMartes: RecyclerView
    private lateinit var recyclerViewTareasMiercoles: RecyclerView
    private lateinit var recyclerViewEventosMiercoles: RecyclerView
    private lateinit var recyclerViewTareasJueves: RecyclerView
    private lateinit var recyclerViewEventosJueves: RecyclerView
    private lateinit var recyclerViewTareasViernes: RecyclerView
    private lateinit var recyclerViewEventosViernes: RecyclerView
    private lateinit var recyclerViewTareasSabado: RecyclerView
    private lateinit var recyclerViewEventosSabado: RecyclerView
    private lateinit var recyclerViewTareasDomingo: RecyclerView
    private lateinit var recyclerViewEventosDomingo: RecyclerView

    private val calendar: Calendar = Calendar.getInstance() // Instancia para controlar la semana

    private lateinit var tareaRepository: TareaRepository
    private lateinit var eventoRepository: EventoRepository
    private lateinit var todasLasTareas: List<Tarea>
    private lateinit var todosLosEventos: List<Evento>
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Formato de fecha

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semana)

        // Inicialización de vistas
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        addEventButton = findViewById(R.id.add_event_button)
        weekTitle = findViewById(R.id.week_title)

        // Configurar RecyclerView de tareas y eventos para cada día
        recyclerViewTareasLunes = findViewById(R.id.recycler_view_lunes)
        recyclerViewEventosLunes = findViewById(R.id.recycler_view_eventos_lunes)
        recyclerViewTareasMartes = findViewById(R.id.recycler_view_martes)
        recyclerViewEventosMartes = findViewById(R.id.recycler_view_eventos_martes)
        recyclerViewTareasMiercoles = findViewById(R.id.recycler_view_miercoles)
        recyclerViewEventosMiercoles = findViewById(R.id.recycler_view_eventos_miercoles)
        recyclerViewTareasJueves = findViewById(R.id.recycler_view_jueves)
        recyclerViewEventosJueves = findViewById(R.id.recycler_view_eventos_jueves)
        recyclerViewTareasViernes = findViewById(R.id.recycler_view_viernes)
        recyclerViewEventosViernes = findViewById(R.id.recycler_view_eventos_viernes)
        recyclerViewTareasSabado = findViewById(R.id.recycler_view_sabado)
        recyclerViewEventosSabado = findViewById(R.id.recycler_view_eventos_sabado)
        recyclerViewTareasDomingo = findViewById(R.id.recycler_view_domingo)
        recyclerViewEventosDomingo = findViewById(R.id.recycler_view_eventos_domingo)

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

        // Configura el NavigationView
        navigationView.setNavigationItemSelectedListener { item ->
            onNavigationItemSelected(item)
        }

        // Inicializar repositorios y cargar todas las tareas y eventos
        tareaRepository = TareaRepository(DatabaseHelper(this))
        eventoRepository = EventoRepository(DatabaseHelper(this))
        todasLasTareas = tareaRepository.obtenerTareas()
        todosLosEventos = eventoRepository.obtenerEvento()

        // Navegación a la vista mensual al presionar "MES"
        findViewById<TextView>(R.id.view_selector_mes).setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.view_selector_dia).setOnClickListener {
            val intent = Intent(this, DiaActivity::class.java)
            startActivity(intent)
        }

        // Configura la navegación entre semanas
        findViewById<TextView>(R.id.previous_week).setOnClickListener {
            navigateWeek(-1)
        }
        findViewById<TextView>(R.id.next_week).setOnClickListener {
            navigateWeek(1)
        }

        // Cargar la semana actual al iniciar la actividad
        loadWeek()
    }

    private fun navigateWeek(offset: Int) {
        calendar.add(Calendar.WEEK_OF_YEAR, offset) // Ajusta la semana actual sumando o restando semanas
        loadWeek() // Recarga la vista con la semana ajustada
    }


    private fun loadWeek() {
        val startOfWeek = calendar.clone() as Calendar
        startOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val endOfWeek = calendar.clone() as Calendar
        endOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val title = "${dateFormat.format(startOfWeek.time)} - ${dateFormat.format(endOfWeek.time)}"
        weekTitle.text = title

        loadWeekDays(startOfWeek)
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

    private fun loadWeekDays(startOfWeek: Calendar) {
        val dayFormat = SimpleDateFormat("EEE dd", Locale.getDefault())
        val dayCalendar = startOfWeek.clone() as Calendar

        // Configura cada día de la semana
        setRecyclerViews(recyclerViewTareasLunes, recyclerViewEventosLunes, dayCalendar, dayFormat)
        dayCalendar.add(Calendar.DAY_OF_WEEK, 1)
        setRecyclerViews(recyclerViewTareasMartes, recyclerViewEventosMartes, dayCalendar, dayFormat)
        dayCalendar.add(Calendar.DAY_OF_WEEK, 1)
        setRecyclerViews(recyclerViewTareasMiercoles, recyclerViewEventosMiercoles, dayCalendar, dayFormat)
        dayCalendar.add(Calendar.DAY_OF_WEEK, 1)
        setRecyclerViews(recyclerViewTareasJueves, recyclerViewEventosJueves, dayCalendar, dayFormat)
        dayCalendar.add(Calendar.DAY_OF_WEEK, 1)
        setRecyclerViews(recyclerViewTareasViernes, recyclerViewEventosViernes, dayCalendar, dayFormat)
        dayCalendar.add(Calendar.DAY_OF_WEEK, 1)
        setRecyclerViews(recyclerViewTareasSabado, recyclerViewEventosSabado, dayCalendar, dayFormat)
        dayCalendar.add(Calendar.DAY_OF_WEEK, 1)
        setRecyclerViews(recyclerViewTareasDomingo, recyclerViewEventosDomingo, dayCalendar, dayFormat)
    }

    private fun setRecyclerViews(
        recyclerViewTareas: RecyclerView,
        recyclerViewEventos: RecyclerView,
        dayCalendar: Calendar,
        dayFormat: SimpleDateFormat
    ) {
        // Filtrar las tareas y eventos específicos del día actual en `dayCalendar`
        val tareas = obtenerTareasPorFecha(dayCalendar.time)
        val tareaSemanaAdapter = TareaCAdapter(tareas)
        recyclerViewTareas.adapter = tareaSemanaAdapter
        recyclerViewTareas.layoutManager = LinearLayoutManager(this)

        val eventos = obtenerEventosPorFecha(dayCalendar.time)
        val eventoSemanaAdapter = EventoCAdapter(eventos)
        recyclerViewEventos.adapter = eventoSemanaAdapter
        recyclerViewEventos.layoutManager = LinearLayoutManager(this)
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



    private fun getDayTextViewId(diaSemana: Int): Int {
        return when (diaSemana) {
            Calendar.MONDAY -> R.id.text_view_lunes
            Calendar.TUESDAY -> R.id.text_view_martes
            Calendar.WEDNESDAY -> R.id.text_view_miercoles
            Calendar.THURSDAY -> R.id.text_view_jueves
            Calendar.FRIDAY -> R.id.text_view_viernes
            Calendar.SATURDAY -> R.id.text_view_sabado
            Calendar.SUNDAY -> R.id.text_view_domingo
            else -> throw IllegalArgumentException("Día de la semana inválido")
        }
    }
    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Redirige a la actividad principal
                val intent = Intent(this, PrincipalActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_task -> {
                // Redirige a la actividad principal
                val intent = Intent(this, ListaActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_evento -> {
                // Redirige a la actividad de lista de tareas
                val intent = Intent(this, ListaEvento::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
