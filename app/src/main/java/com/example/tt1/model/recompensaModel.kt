// RecompensaModel.kt
package com.example.tt1.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.tt1.DatabaseHelper

class RecompensaModel(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun getPuntosTotales(idUsuario: Int): Int {
        val db = dbHelper.readableDatabase
        var puntosTotales = 0

        val cursor: Cursor = db.rawQuery(
            "SELECT SUM(puntos) FROM Recompensa WHERE idUsuario = ?",
            arrayOf(idUsuario.toString())
        )

        if (cursor.moveToFirst()) {
            puntosTotales = cursor.getInt(0)
        }
        cursor.close()
        db.close()

        return puntosTotales
    }

    fun getLogros(idUsuario: Int): List<String> {
        val db = dbHelper.readableDatabase
        val logros = mutableListOf<String>()

        val cursor: Cursor = db.rawQuery(
            """SELECT Logro.Nombre FROM Recompensa 
               INNER JOIN Logro ON Recompensa.IdLogro = Logro.idLogro
               WHERE Recompensa.idUsuario = ?""",
            arrayOf(idUsuario.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                logros.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return logros
    }

    fun agregarRecompensa(puntos: Int, idLogro: Int, idUsuario: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("puntos", puntos)
            put("IdLogro", idLogro)
            put("idUsuario", idUsuario)
        }
        db.insert("Recompensa", null, values)
        db.close()
    }
}
