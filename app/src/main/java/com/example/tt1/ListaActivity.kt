package com.example.tt1
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar

class ListaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        // Configuración de la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Configuración del DrawerLayout y NavigationView
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        // Configuración del botón de navegación en la Toolbar
        toolbar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        // Configuración del NavigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Manejar los clicks de los ítems del menú
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Redirige a la actividad principal
                    val intent = Intent(this, PrincipalActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_calendar -> {
                    // Redirige a la actividad de calendario
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                }
                // Agrega más casos según los ítems del menú
            }
            // Cerrar el menú al seleccionar un ítem
            drawerLayout.close()
            true
        }
    }
}
