package com.example.tt1.controller

import android.view.View
import com.example.tt1.model.LoginModel
import com.example.tt1.view.LoginView

class LoginController(
    private val model: LoginModel,
    private val view: LoginView
) {

    init {
        view.setLoginButtonClickListener(View.OnClickListener {
            handleLogin()
        })
    }

    private fun handleLogin() {
        model.email = view.getEmail()
        model.password = view.getPassword()

        // Permitir inicio de sesión sin validar email o contraseña
        view.showSuccess("Inicio de sesión exitoso.")
        onLoginSuccessListener?.invoke()  // Notificar el éxito del login
    }

    private var onLoginSuccessListener: (() -> Unit)? = null

    fun setOnLoginSuccessListener(listener: () -> Unit) {
        onLoginSuccessListener = listener
    }
}
