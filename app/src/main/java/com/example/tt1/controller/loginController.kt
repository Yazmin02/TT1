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

        when {
            !model.isValidEmail() -> view.showError("Correo electrónico no válido.")
            !model.isValidPassword() -> view.showError("La contraseña debe tener al menos 6 caracteres.")
            else -> {
                // Aquí podrías manejar la lógica de autenticación, como llamar a una API
                view.showSuccess("Inicio de sesión exitoso.")
            }
        }
    }
}
