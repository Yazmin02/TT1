package com.example.tt1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Asegúrate de que este sea el layout correcto

        // Inicializa las vistas
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        registerButton = findViewById(R.id.register_button)

        // Configura el botón de registro
        registerButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Aquí podrías añadir la lógica para registrar el usuario, por ejemplo:
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Simular registro exitoso
                showMessage("Registro exitoso. Bienvenido, $email!")
            } else {
                showError("Por favor, completa todos los campos.")
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
