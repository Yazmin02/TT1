package com.example.tt1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.model.UsuarioRepository

class MainActivity : AppCompatActivity() {
    private lateinit var usuarioRepo: UsuarioRepository
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializa el DatabaseHelper y el UsuarioRepository
        val dbHelper = DatabaseHelper(this)
        usuarioRepo = UsuarioRepository(dbHelper)

        // Inicializa las vistas
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)
        registerLink = findViewById(R.id.register_link)

        // Configura el botón de inicio de sesión
        loginButton.setOnClickListener {
            val correoE = emailInput.text.toString().trim()
            val contraseña = passwordInput.text.toString().trim()

            // Llama al método authenticate del repositorio
            val idUsuario = usuarioRepo.authenticate(correoE, contraseña)

            if (idUsuario != null) {
                // Inicio de sesión exitoso, redirige a la siguiente actividad
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PrincipalActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Mostrar mensaje de error
                showError("Credenciales inválidas")
            }
        }

        // Configura el enlace de registro
        registerLink.setOnClickListener {
            // Navegar a la actividad de registro
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // Muestra un mensaje de error
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
