// TareaDao.kt
package com.example.tt1.repository

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tt1.model.Tarea

class TareaDao(private val db: SQLiteDatabase) {

    fun insertarTarea(tarea: Tarea) {
        val values = ContentValues().apply {
            put("titulo", tarea.titulo)
            put("descripcion", tarea.descripcion)
            put("fInicio", tarea.fInicio)
            put("fVencimiento", tarea.fVencimiento)
            put("idUsuario", tarea.idUsuario)
            put("idEtiqueta", tarea.idEtiqueta)
        }
        db.insert("Tarea", null, values)
    }

    fun obtenerTareasPorUsuario(idUsuario: Int): List<Tarea> {
        val tareas = mutableListOf<Tarea>()
        val cursor: Cursor = db.query(
            "Tarea",
            null,
            "idUsuario = ?",
            arrayOf(idUsuario.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val tarea = Tarea(
                    idTarea = getInt(getColumnIndexOrThrow("idTarea")),
                    titulo = getString(getColumnIndexOrThrow("titulo")),
                    descripcion = getString(getColumnIndexOrThrow("descripcion")),
                    fInicio = getString(getColumnIndexOrThrow("fInicio")),
                    fVencimiento = getString(getColumnIndexOrThrow("fVencimiento")),
                    idUsuario = getInt(getColumnIndexOrThrow("idUsuario")),
                    idEtiqueta = getInt(getColumnIndexOrThrow("idEtiqueta"))
                )
                tareas.add(tarea)
            }
        }
        cursor.close()
        return tareas
    }
}
