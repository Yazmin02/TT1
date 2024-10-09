package com.example.tt1.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tt1.DatabaseHelper

class UsuarioRepository(private val dbHelper: DatabaseHelper) {

    // Método para agregar un nuevo usuario
    fun agregarUsuario(usuario: Usuario) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nUsuario", usuario.nUsuario)
            put("correoE", usuario.correoE)
            put("contraseña", usuario.password)
        }
        db.insert("Usuario", null, values)
        db.close()
    }

    // Método para autenticar un usuario
    @SuppressLint("Range")
    fun authenticate(correoE: String, contraseña: String): Int? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "Usuario",
            arrayOf("idUsuario"),
            "correoE = ? AND contraseña = ?",
            arrayOf(correoE, contraseña),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex("idUsuario"))
            cursor.close()
            id // Devuelve el ID del usuario
        } else {
            cursor.close()
            null // Si no se encontró, retorna null
        }
    }

    // Método para obtener todos los usuarios (opcional)
    @SuppressLint("Range")
    fun obtenerUsuarios(): List<Usuario> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "Usuario",
            null,
            null,
            null,
            null,
            null,
            null
        )
        val usuarios = mutableListOf<Usuario>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("idUsuario"))
            val nombre = cursor.getString(cursor.getColumnIndex("nUsuario"))
            val email = cursor.getString(cursor.getColumnIndex("correoE"))
            val password = cursor.getString(cursor.getColumnIndex("contraseña"))
            usuarios.add(Usuario(id, nombre, email, password))
        }
        cursor.close()
        return usuarios
    }
}
