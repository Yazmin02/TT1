package com.example.tt1

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.controller.RegisterController

class RegisterActivity : AppCompatActivity() {

    private lateinit var nUsuarioInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerButton: Button

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Asegúrate de que este sea el layout correcto

        // Inicializa las vistas
        nUsuarioInput = findViewById(R.id.email_input) // Corrige el ID
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        registerButton = findViewById(R.id.register_button)

        // Configura el botón de registro
        registerButton.setOnClickListener {
            val nUsuario = nUsuarioInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (nUsuario.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val registerController = RegisterController(this)
                registerController.registerUser(nUsuario, email, password,
                    onSuccess = {
                        showMessage("Registro exitoso. Bienvenido, $nUsuario!")
                    },
                    onError = { errorMessage ->
                        showError(errorMessage)
                    }
                )
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
