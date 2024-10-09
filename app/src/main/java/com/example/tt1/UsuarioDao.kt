// UsuarioDao.kt
package com.example.tt1

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.tt1.model.Usuario

class UsuarioDao(private val db: SQLiteDatabase) {

    fun insertarUsuario(usuario: Usuario) {
        val valores = ContentValues().apply {
            put("nUsuario", usuario.nUsuario)  // Asegúrate de que el nombre del campo sea correcto
            put("correoE", usuario.correoE)
            put("contraseña", usuario.password)
        }
        db.insert("Usuario", null, valores)
    }

    fun obtenerUsuarioPorCorreo(correo: String): Usuario? {
        var usuario: Usuario? = null
        val cursor = db.query(
            "Usuario",
            null,
            "correoE = ?",
            arrayOf(correo),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            usuario = Usuario(
                nUsuario = cursor.getString(cursor.getColumnIndexOrThrow("nUsuario")),
                correoE = cursor.getString(cursor.getColumnIndexOrThrow("correoE")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("contraseña"))
            )
        }
        cursor.close()
        return usuario
    }

    fun usuarioExists(correo: String): Boolean {
        val cursor = db.query(
            "Usuario",
            null,
            "correoE = ?",
            arrayOf(correo),
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}
