package com.example.tt1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat

class CalendarActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var addEventButton: Button
    private lateinit var calendarView: CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        addEventButton = findViewById(R.id.add_event_button)
        calendarView = findViewById(R.id.calendar_view)

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
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_add_task -> {
                    // Redirigir a la pantalla de tarea
                    val intent = Intent(this, TareaActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_add_event -> {
                    // Lógica para agregar evento
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Redirige a la actividad principal
                val intent = Intent(this, PrincipalActivity::class.java)
                startActivity(intent)
            }
            // Otros ítems del menú pueden ser manejados aquí
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
