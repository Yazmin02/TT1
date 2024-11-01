package com.example.tt1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tt1.controller.LogrosAdapter
import com.example.tt1.evento.ListaEvento
import com.example.tt1.model.RecompensaModel
import com.example.tt1.tarea.ListaActivity
import com.google.android.material.navigation.NavigationView

class RecompensasActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var recompensaModel: RecompensaModel
    private lateinit var logrosAdapter: LogrosAdapter
    private lateinit var drawerLayout: DrawerLayout


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recompensa)

        recompensaModel = RecompensaModel(this)

        // Asigna logros para el usuario
        val idUsuario = 1
        recompensaModel.verificarLogros(idUsuario)

        // Configura la lista de logros
        val rvLogros = findViewById<RecyclerView>(R.id.rvLogros)
        rvLogros.layoutManager = LinearLayoutManager(this)
        actualizarPuntosYLogros(idUsuario)

        // Configuración de la Toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Configuración del DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onResume() {
        super.onResume()
        actualizarPuntosYLogros(1) // idUsuario = 1
    }

    private fun actualizarPuntosYLogros(idUsuario: Int) {
        // Muestra los puntos totales
        val puntosTotales = recompensaModel.getPuntosTotales(idUsuario)
        findViewById<TextView>(R.id.tvPuntosTotales).text = "Puntos Totales: $puntosTotales"

        // Muestra el nivel actual
        val nivelActual = recompensaModel.getNivelUsuario(puntosTotales)
        findViewById<TextView>(R.id.tvNivelUsuario).text = "Nivel: $nivelActual"

        // Configura la barra de progreso
        val progresoSiguienteLogro = findViewById<ProgressBar>(R.id.progresoSiguienteLogro)
        progresoSiguienteLogro.progress = calcularProgreso(puntosTotales)

        // Muestra los logros
        val logros = recompensaModel.getLogros(idUsuario)
        logrosAdapter = LogrosAdapter(logros)
        findViewById<RecyclerView>(R.id.rvLogros).adapter = logrosAdapter
    }



    private fun calcularProgreso(puntosTotales: Int): Int {
        // Define los puntos necesarios para cada rango de nivel
        val (nivelActualPuntos, siguienteNivelPuntos) = when {
            puntosTotales <= 500 -> 0 to 500
            puntosTotales <= 1000 -> 501 to 1000
            puntosTotales <= 2000 -> 1001 to 2000
            puntosTotales <= 3000 -> 2001 to 3000
            else -> 3001 to 4000
        }

        // Calcula el progreso relativo dentro del rango del nivel actual
        val progresoRelativo = puntosTotales - nivelActualPuntos
        val rangoNivel = siguienteNivelPuntos - nivelActualPuntos
        return (progresoRelativo * 100) / rangoNivel
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_calendar -> {
                // Redirige a la actividad de calendario
                val intent = Intent(this, CalendarActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_task -> {
                // Redirige a la actividad de lista de tareas
                val intent = Intent(this, ListaActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_evento -> {
                // Redirige a la actividad de lista de tareas
                val intent = Intent(this, ListaEvento::class.java)
                startActivity(intent)
            }
            R.id.nav_home -> {
                // Redirige a la actividad de lista de tareas
                val intent = Intent(this, PrincipalActivity::class.java)
                startActivity(intent)
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
