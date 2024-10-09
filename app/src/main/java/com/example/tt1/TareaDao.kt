package com.example.tt1

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tt1.model.Tarea

class TareaDao(private val db: SQLiteDatabase) {

    // Insertar una nueva tarea
    fun insertarTarea(tarea: Tarea): Long {
        val values = ContentValues().apply {
            put("titulo", tarea.titulo)
            put("descripcion", tarea.descripcion)
            put("fInicio", tarea.fInicio)
            put("fVencimiento", tarea.fVencimiento)
            put("idEtiqueta", tarea.idEtiqueta)
            put("idUsuario", tarea.idUsuario) // Asegúrate de incluir idUsuario
        }
        // Insertar tarea en la base de datos. SQLite se encargará de generar el ID.
        return db.insert("Tarea", null, values)
    }

    // Obtener todas las tareas
    fun obtenerTodasLasTareas(): List<Tarea> {
        val tareas = mutableListOf<Tarea>()
        val cursor: Cursor = db.query("Tarea", null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val tarea = Tarea(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")), // Asegúrate de incluir el ID
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                    fInicio = cursor.getString(cursor.getColumnIndexOrThrow("fInicio")),
                    fVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fVencimiento")),
                    idEtiqueta = cursor.getInt(cursor.getColumnIndexOrThrow("idEtiqueta")),
                    idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("idUsuario")) // Incluye idUsuario
                )
                tareas.add(tarea)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tareas
    }

    // Eliminar una tarea por su ID
    fun eliminarTarea(idTarea: Int) {
        db.delete("Tarea", "id = ?", arrayOf(idTarea.toString())) // Asegúrate de que el nombre de la columna sea "id"
    }

    // Obtener tareas por usuario (si la relación usuario-tarea existe en tu esquema)
    fun obtenerTareasPorUsuario(idUsuario: Int): List<Tarea> {
        val tareas = mutableListOf<Tarea>()
        val cursor = db.query(
            "Tarea",
            null,
            "idUsuario = ?",
            arrayOf(idUsuario.toString()),
            null,
            null,
            "fVencimiento ASC"
        )

        if (cursor.moveToFirst()) {
            do {
                val tarea = Tarea(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")), // Asegúrate de incluir el ID
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                    fInicio = cursor.getString(cursor.getColumnIndexOrThrow("fInicio")),
                    fVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fVencimiento")),
                    idEtiqueta = cursor.getInt(cursor.getColumnIndexOrThrow("idEtiqueta")),
                    idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("idUsuario"))
                )
                tareas.add(tarea)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tareas
    }
}
