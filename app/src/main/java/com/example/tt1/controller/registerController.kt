package com.example.tt1.controller

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.tt1.DatabaseHelper
import com.example.tt1.model.Usuario

class RegisterController(private val context: Context) {

    fun registerUser(nUsuario: String, email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val usuario = Usuario(
            idUsuario = 0, // El ID se generará automáticamente
            nUsuario = nUsuario,
            email = email,
            password = password
        )

        val dbHelper = DatabaseHelper(context) // Crea una instancia de tu helper

        // Inserta el usuario en la base de datos
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nUsuario", usuario.nUsuario)
            put("correoE", usuario.email)
            put("contraseña", usuario.password)
        }

        val result = db.insert("Usuario", null, values) // Inserta en la tabla Usuario

        if (result != -1L) {
            Log.d("RegisterController", "Usuario registrado con éxito: ${usuario.nUsuario}")
            onSuccess() // Llama a la función de éxito si la inserción fue exitosa
        } else {
            Log.e("RegisterController", "Error al registrar el usuario")
            onError("Error al registrar el usuario") // Llama a la función de error si falló
        }

        db.close() // Cierra la base de datos
    }
}
