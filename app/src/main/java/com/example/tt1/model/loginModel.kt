package com.example.tt1.model

data class LoginModel(
    var email: String = "",
    var password: String = ""
) {
    fun isValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(): Boolean {
        // Validar que la contraseña tenga al menos 6 caracteres
        return password.length >= 6
    }
}
