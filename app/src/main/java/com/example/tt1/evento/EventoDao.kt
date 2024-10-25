package com.example.tt1.evento

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.tt1.model.entidades.Evento

class EventoDao(private val db: SQLiteDatabase) {
    fun insertarEvento(evento: Evento): Long {
        return try {
            val values = ContentValues().apply {
                put("titulo", evento.titulo)
                put("descripcion", evento.descripcion)
                put("fInicio", evento.fInicio)
                put("fVencimiento", evento.fVencimiento)
                put("idEtiqueta", evento.idEtiqueta)
                put("lugar", evento.lugar)
            }
            // Insertar evento en la base de datos. SQLite se encargará de generar el ID.
            db.insertOrThrow("Evento", null, values) // Usar insertOrThrow para manejar excepciones
        } catch (e: Exception) {
            Log.e("EventoDao", "Error al insertar evento: ${e.message}", e)
            -1 // Devuelve -1 o algún valor indicativo de error
        }
    }

    fun obtenerTodosLosEventos(): List<Evento> {
        val eventos = mutableListOf<Evento>()
        var cursor: Cursor? = null

        try {
            cursor = db.query("Evento", null, null, null, null, null, null)

            if (cursor.moveToFirst()) {
                do {
                    val evento = Evento(
                        idEvento = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                        descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                        fInicio = cursor.getString(cursor.getColumnIndexOrThrow("fInicio")),
                        fVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fVencimiento")),
                        idEtiqueta = cursor.getInt(cursor.getColumnIndexOrThrow("idEtiqueta")),
                        lugar =  cursor.getString(cursor.getColumnIndexOrThrow("lugar")),
                        idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("idUsuario"))
                    )
                    eventos.add(evento)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("TareaDao", "Error al obtener los eventos: ${e.message}", e)
        } finally {
            cursor?.close() // Asegúrate de cerrar el cursor en el bloque finally
        }

        return eventos
    }

    // Eliminar una evento por su ID
    fun eleiminarEvento(idEvento: Int): Boolean {
        return try {
            val rowsAffected = db.delete("Evento", "idEvento = ?", arrayOf(idEvento.toString())) // Asegúrate de que el nombre de la columna sea "id"
            rowsAffected > 0 // Devuelve true si se eliminó al menos una fila
        } catch (e: Exception) {
            Log.e("EventoDao", "Error al eliminar el evento: ${e.message}", e)
            false // Devuelve false si ocurrió un error
        }
    }

    // Obtener eventos por usuario
    fun obtenerEventos(idUsuario: Int): List<Evento> {
        val eventos = mutableListOf<Evento>()
        var cursor: Cursor? = null

        try {
            cursor = db.query(
                "Evento",
                null,
                "idUsuario = ?",
                arrayOf(idUsuario.toString()),
                null,
                null,
                "fVencimiento ASC" // Ordenar por fecha de vencimiento
            )

            if (cursor.moveToFirst()) {
                do {
                    val evento = Evento(
                        idEvento = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                        descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                        fInicio = cursor.getString(cursor.getColumnIndexOrThrow("fInicio")),
                        fVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fVencimiento")),
                        idEtiqueta = cursor.getInt(cursor.getColumnIndexOrThrow("idEtiqueta")),
                        lugar =  cursor.getString(cursor.getColumnIndexOrThrow("lugar")),
                        idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("idUsuario"))
                    )
                    eventos.add(evento)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("EventoDao", "Error al obtener eventos por usuario: ${e.message}", e)
        } finally {
            cursor?.close() // Asegúrate de cerrar el cursor en el bloque finally
        }

        return eventos
    }
}
