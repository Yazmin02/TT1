package com.example.tt1.evento

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.CalendarActivity
import com.example.tt1.DatabaseHelper
import com.example.tt1.PrincipalActivity
import com.example.tt1.R
import com.example.tt1.model.entidades.Evento
import com.example.tt1.model.repositorios.EventoRepository
import com.google.android.material.navigation.NavigationView


class ListaEvento : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventoAdapter: EventoAdapter
    private lateinit var eventoRepository: EventoRepository

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listae)

        setupToolbar() // Inicializa el Toolbar
        setupNavigationDrawer() // Inicializa el NavigationView

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewEventos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar el repositorio
        eventoRepository = EventoRepository(DatabaseHelper(this))

        // Obtener las tareas y convertir a MutableList
        val eventos: MutableList<Evento> = eventoRepository.obtenerEvento().toMutableList()

        eventoAdapter = EventoAdapter(eventos,
            onVerClick = { evento ->
                // Acción al hacer clic en el botón "Ver" de la tarea
                val intent = Intent(this, VerEventoActivity::class.java)
                intent.putExtra("idEvento", evento.idEvento) // Pasar el ID de la tarea
                startActivityForResult(intent, REQUEST_CODE_VER_EVENTO) // Usar startActivityForResult
            },
            onEditarClick = { evento ->
                // Implementa la acción de edición aquí si es necesario
            },
            onEliminarClick = { evento ->
                // Eliminar la tarea usando su ID
                eventoRepository.eliminarEvento(evento.idEvento)
                actualizarListaEventos() // Llama para actualizar la lista después de eliminar
            }
        )

        recyclerView.adapter = eventoAdapter
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

    private fun actualizarListaEventos() {
        // Actualizar la lista de tareas después de una eliminación
        val eventosActualizados: MutableList<Evento> = eventoRepository.obtenerEvento().toMutableList()
        eventoAdapter.updateEventos(eventosActualizados) // Actualiza la lista en el adaptador
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_VER_EVENTO && resultCode == RESULT_OK) {
            actualizarListaEventos() // Actualiza la lista al volver de VerTareaActivity
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
        const val REQUEST_CODE_VER_EVENTO = 1 // Código de solicitud para la actividad VerTarea
    }
}
