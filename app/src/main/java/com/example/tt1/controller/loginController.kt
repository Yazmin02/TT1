package com.example.tt1.controller

import android.content.Intent
import com.example.tt1.MainActivity
import com.example.tt1.PrincipalActivity
import com.example.tt1.model.LoginModel
import com.example.tt1.model.UsuarioRepository

class LoginController(private val activity: MainActivity) {
    private val usuariosRepository = UsuarioRepository()

    fun handleLogin(email: String, password: String) {
        val loginModel = LoginModel(email, password)

        // Validar el email y la contraseña
        if (!loginModel.isValidEmail()) {
            activity.showError("Por favor, introduce un correo electrónico válido.")
            return
        }

        if (!loginModel.isValidPassword()) {
            activity.showError("La contraseña debe tener al menos 6 caracteres.")
            return
        }

        // Lógica de autenticación
        val userId = usuariosRepository.authenticate(email, password)
        if (userId != null) {
            loginModel.idUsuario = userId
            activity.showMessage("Inicio de sesión exitoso")

            // Navegar a la pantalla principal (PrincipalActivity)
            val intent = Intent(activity, PrincipalActivity::class.java)
            activity.startActivity(intent)
            activity.finish() // Cierra esta actividad
        } else {
            activity.showError("Credenciales incorrectas")
        }
    }
}
