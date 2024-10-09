package com.example.tt1.controller

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.example.tt1.DatabaseHelper

class RegisterController(private val context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun registerUser(username: String, email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val db: SQLiteDatabase = dbHelper.writableDatabase

        // Validar que el nombre de usuario y el correo no estén vacíos
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            onError("Todos los campos son obligatorios")
            return
        }

        // Validar que el correo tenga un formato correcto (opcional)
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onError("Correo no válido")
            return
        }

        try {
            // Comprobar si el correo ya está registrado
            val query = "SELECT * FROM Usuario WHERE correoE = ?"
            val cursor = db.rawQuery(query, arrayOf(email))

            if (cursor.count > 0) {
                onError("El correo ya está registrado")
                cursor.close()
                return
            }
            cursor.close()

            // Insertar el nuevo usuario usando las columnas correctas
            val insertQuery = "INSERT INTO Usuario (nUsuario, correoE, contraseña) VALUES (?, ?, ?)"
            db.execSQL(insertQuery, arrayOf(username, email, password))
            onSuccess()
        } catch (e: SQLiteException) {
            onError("Error al registrar: ${e.message}")
        } finally {
            db.close()
        }
    }
}
