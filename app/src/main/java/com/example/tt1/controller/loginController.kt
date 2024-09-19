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
        // Obtener los valores de los campos de email y contraseña desde la vista
        model.email = view.getEmail()
        model.password = view.getPassword()

        // Validar si los campos están vacíos
        if (model.email.isEmpty()) {
            view.showError("El campo de correo no puede estar vacío.")
            return
        }

        if (model.password.isEmpty()) {
            view.showError("El campo de contraseña no puede estar vacío.")
            return
        }

        // Si ambos campos están completos, permitir el inicio de sesión
        view.showSuccess("Inicio de sesión exitoso.")
        onLoginSuccessListener?.invoke()  // Notificar el éxito del login
    }

    private var onLoginSuccessListener: (() -> Unit)? = null

    fun setOnLoginSuccessListener(listener: () -> Unit) {
        onLoginSuccessListener = listener
    }
}
