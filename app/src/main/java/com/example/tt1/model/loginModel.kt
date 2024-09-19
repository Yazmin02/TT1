package com.example.tt1.model

data class LoginModel(
    var email: String = "",
    var password: String = "",
    var idUsuario: Int = 0 // agregar el id del usuario logueado
) {
    fun isValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(): Boolean {
        return password.length >= 6
    }
}
