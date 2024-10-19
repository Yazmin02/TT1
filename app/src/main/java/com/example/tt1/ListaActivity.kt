package com.example.tt1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.model.Tarea
import com.google.android.material.navigation.NavigationView

class ListaActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tareaAdapter: TareaAdapter
    private lateinit var tareaRepository: TareaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        setupToolbar() // Inicializa el Toolbar
        setupNavigationDrawer() // Inicializa el NavigationView

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTareas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar el repositorio
        tareaRepository = TareaRepository(DatabaseHelper(this))

        // Obtener las tareas y convertir a MutableList
        val tareas: MutableList<Tarea> = tareaRepository.obtenerTareas().toMutableList()

        tareaAdapter = TareaAdapter(tareas,
            onVerClick = { tarea ->
                // Acción al hacer clic en el botón "Ver" de la tarea
                val intent = Intent(this, VerTareaActivity::class.java)
                intent.putExtra("idTarea", tarea.id) // Pasar el ID de la tarea
                startActivityForResult(intent, REQUEST_CODE_VER_TAREA) // Usar startActivityForResult
            },
            onEditarClick = { tarea ->
                // Implementa la acción de edición aquí si es necesario
            },
            onEliminarClick = { tarea ->
                // Eliminar la tarea usando su ID
                tareaRepository.eliminarTarea(tarea.id)
                actualizarListaTareas() // Llama para actualizar la lista después de eliminar
            }
        )

        recyclerView.adapter = tareaAdapter
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.ic_menu) // Añade el ícono de menú
        toolbar.setNavigationOnClickListener {
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setupNavigationDrawer() {
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun actualizarListaTareas() {
        // Actualizar la lista de tareas después de una eliminación
        val tareasActualizadas: MutableList<Tarea> = tareaRepository.obtenerTareas().toMutableList()
        tareaAdapter.updateTareas(tareasActualizadas) // Actualiza la lista en el adaptador
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_VER_TAREA && resultCode == RESULT_OK) {
            actualizarListaTareas() // Actualiza la lista al volver de VerTareaActivity
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_calendar -> {
                startActivity(Intent(this, CalendarActivity::class.java))
            }
            R.id.nav_home -> {
                startActivity(Intent(this, PrincipalActivity::class.java))
            }
        }
        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        return true
    }

    companion object {
        const val REQUEST_CODE_VER_TAREA = 1 // Código de solicitud para la actividad VerTarea
    }
}
