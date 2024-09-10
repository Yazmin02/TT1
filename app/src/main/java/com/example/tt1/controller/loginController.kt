package com.example.tt1.controller

import android.content.Context
import android.widget.Toast
import com.example.tt1.model.loginModel

class LoginController(private val context: Context) {

    fun handleLogin(email: String, password: String) {
        val user = loginModel(email, password)

        if (validateUser(user)) {
            // Lógica para iniciar sesión
            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateUser(user: loginModel): Boolean {
        // Validaciones básicas
        return user.email.isNotEmpty() && user.password.isNotEmpty()
    }
}
