package com.example.tt1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.model.Tarea
import com.google.android.material.navigation.NavigationView

class ListaTareasActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tareaAdapter: TareaAdapter
    private lateinit var tareaRepository: TareaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        // Configuración del DrawerLayout y el ActionBarToggle
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Manejo de clics en el menú
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_inicio -> {
                    startActivity(Intent(this, PrincipalActivity::class.java))
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_calendar -> {
                    startActivity(Intent(this, CalendarActivity::class.java))
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_task -> {
                    // No hacer nada, ya estamos en esta actividad
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }

        // Inicializa la base de datos y el repositorio
        val dbHelper = DatabaseHelper(this)
        tareaRepository = TareaRepository(dbHelper)

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTareas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Carga las tareas y configura el adapter
        cargarTareas()
    }

    private fun cargarTareas() {
        val tareas: List<Tarea> = tareaRepository.obtenerTareas()
        tareaAdapter = TareaAdapter(tareas)
        recyclerView.adapter = tareaAdapter
    }
}
