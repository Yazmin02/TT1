package com.example.tt1

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.tt1.model.Tarea

class TareaDao(private val db: SQLiteDatabase) {

    // Insertar una nueva tarea
    fun insertarTarea(tarea: Tarea): Long {
        return try {
            val values = ContentValues().apply {
                put("titulo", tarea.titulo)
                put("descripcion", tarea.descripcion)
                put("fInicio", tarea.fInicio)
                put("fVencimiento", tarea.fVencimiento)
                put("idEtiqueta", tarea.idEtiqueta)
                put("idUsuario", tarea.idUsuario) // Asegúrate de incluir idUsuario
            }
            // Insertar tarea en la base de datos. SQLite se encargará de generar el ID.
            db.insertOrThrow("Tarea", null, values) // Usar insertOrThrow para manejar excepciones
        } catch (e: Exception) {
            Log.e("TareaDao", "Error al insertar tarea: ${e.message}", e)
            -1 // Devuelve -1 o algún valor indicativo de error
        }
    }

    // Obtener todas las tareas
    fun obtenerTodasLasTareas(): List<Tarea> {
        val tareas = mutableListOf<Tarea>()
        var cursor: Cursor? = null

        try {
            cursor = db.query("Tarea", null, null, null, null, null, null)

            if (cursor.moveToFirst()) {
                do {
                    val tarea = Tarea(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
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
        } catch (e: Exception) {
            Log.e("TareaDao", "Error al obtener tareas: ${e.message}", e)
        } finally {
            cursor?.close() // Asegúrate de cerrar el cursor en el bloque finally
        }

        return tareas
    }

    // Eliminar una tarea por su ID
    fun eliminarTarea(idTarea: Int): Boolean {
        return try {
            val rowsAffected = db.delete("Tarea", "id = ?", arrayOf(idTarea.toString())) // Asegúrate de que el nombre de la columna sea "id"
            rowsAffected > 0 // Devuelve true si se eliminó al menos una fila
        } catch (e: Exception) {
            Log.e("TareaDao", "Error al eliminar tarea: ${e.message}", e)
            false // Devuelve false si ocurrió un error
        }
    }

    // Obtener tareas por usuario (si la relación usuario-tarea existe en tu esquema)
    fun obtenerTareasPorUsuario(idUsuario: Int): List<Tarea> {
        val tareas = mutableListOf<Tarea>()
        var cursor: Cursor? = null

        try {
            cursor = db.query(
                "Tarea",
                null,
                "idUsuario = ?",
                arrayOf(idUsuario.toString()),
                null,
                null,
                "fVencimiento ASC" // Ordenar por fecha de vencimiento
            )

            if (cursor.moveToFirst()) {
                do {
                    val tarea = Tarea(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
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
        } catch (e: Exception) {
            Log.e("TareaDao", "Error al obtener tareas por usuario: ${e.message}", e)
        } finally {
            cursor?.close() // Asegúrate de cerrar el cursor en el bloque finally
        }

        return tareas
    }
}
