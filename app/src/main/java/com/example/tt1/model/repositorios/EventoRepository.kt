package com.example.tt1.model.repositorios

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.tt1.DatabaseHelper
import com.example.tt1.model.entidades.Evento

class EventoRepository(private val dbHelper: DatabaseHelper) {

    fun agregarEvento(evento: Evento) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("titulo", evento.titulo)
            put("descripcion", evento.descripcion)
            put("fInicio", evento.fInicio)
            put("fVencimiento", evento.fVencimiento)
            put("lugar", evento.lugar)
            put("idEtiqueta", evento.idEtiqueta)
            put("estado", evento.estado) // Asegúrate de incluir el estado
        }

        val newRowId = db.insert("Evento", null, values)
        if (newRowId == -1L) {
            Log.e("EventoRepository", "Error al insertar el evento: ${evento.titulo}")
            throw Exception("Error al insertar el evento")
        } else {
            Log.d("EventoRepository", "Evento insertado con éxito: ID = $newRowId")
        }

        db.close()
    }

    @SuppressLint("Range")
    fun obtenerEvento(): List<Evento> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "Evento",
            null,
            null,
            null,
            null,
            null,
            null
        )
        val eventos = mutableListOf<Evento>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("idEvento"))
            val titulo = cursor.getString(cursor.getColumnIndex("titulo"))
            val descripcion = cursor.getString(cursor.getColumnIndex("descripcion"))
            val fInicio = cursor.getString(cursor.getColumnIndex("fInicio"))
            val fVencimiento = cursor.getString(cursor.getColumnIndex("fVencimiento"))
            val idEtiqueta = cursor.getInt(cursor.getColumnIndex("idEtiqueta"))
            val lugar = cursor.getString(cursor.getColumnIndex("lugar"))
            val estado = cursor.getInt(cursor.getColumnIndex("estado")) // Obteniendo el estado

            val evento = Evento(id, titulo, descripcion, fInicio, fVencimiento, estado, idEtiqueta, idUsuario = 1, lugar)
            eventos.add(evento)
        }

        cursor.close()
        db.close()
        return eventos
    }

    @SuppressLint("Range")
    fun obtenerEventoPorId(id: Int): Evento? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "Evento",
            null,
            "idEvento=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val titulo = cursor.getString(cursor.getColumnIndex("titulo"))
            val descripcion = cursor.getString(cursor.getColumnIndex("descripcion"))
            val fInicio = cursor.getString(cursor.getColumnIndex("fInicio"))
            val fVencimiento = cursor.getString(cursor.getColumnIndex("fVencimiento"))
            val idEtiqueta = cursor.getInt(cursor.getColumnIndex("idEtiqueta"))
            val lugar = cursor.getString(cursor.getColumnIndex("lugar"))
            val estado = cursor.getInt(cursor.getColumnIndex("estado"))

            Evento(id, titulo, descripcion, fInicio, fVencimiento, estado, idEtiqueta, idUsuario = 1, lugar)
        } else {
            null
        }.also {
            cursor.close()
        }
    }

    fun eliminarEvento(id: Int) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val deletedRows = db.delete("Evento", "idEvento = ?", arrayOf(id.toString()))

        if (deletedRows > 0) {
            Log.d("EventoRepository", "Evento eliminado con éxito: ID = $id")
        } else {
            Log.e("EventoRepository", "Error al eliminar el evento: ID = $id")
            throw Exception("Error al eliminar el evento")
        }

        db.close()
    }


    fun actualizarEvento(evento: Evento) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("titulo", evento.titulo)
            put("descripcion", evento.descripcion)
            put("fInicio", evento.fInicio)
            put("fVencimiento", evento.fVencimiento)
            put("idEtiqueta", evento.idEtiqueta)
            put("estado", evento.estado) // Asegúrate de incluir el estado

        }

        val updatedRows = db.update("Evento", values, "idEvento = ?", arrayOf(evento.idEvento.toString()))

        if (updatedRows > 0) {
            Log.d("EventoRepository", "Evento actualizado con éxito: ID = ${evento.idEvento}")
        } else {
            Log.e("EventoRepository", "Error al actualizar el evento: ID = ${evento.idEvento}")
            throw Exception("Error al actualizar el evento")
        }

        db.close()
    }

    fun marcarEventoComoCompleto(id: Int) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("estado", 1) // Cambiar estado a completada
        }

        val updatedRows = db.update("Evento", values, "idEvento = ?", arrayOf(id.toString()))

        if (updatedRows > 0) {
            Log.d("EventoRepository", "Evento marcado como completado: ID = $id")
        } else {
            Log.e("EventoRepository", "Error al marcar el evento como completado: ID = $id")
            throw Exception("Error al marcar el evento como completado")
        }

        db.close()
    }
    fun obtenerNombreEtiquetaPorId(idEtiqueta: Int): String? {
        val db: SQLiteDatabase = dbHelper.readableDatabase

        var nombre: String? = null

        val cursor = db.rawQuery("SELECT nombre FROM Etiqueta WHERE idEtiqueta = ?", arrayOf(idEtiqueta.toString()))
        if (cursor.moveToFirst()) {
            nombre = cursor.getString(0) // Obtener el nombre de la etiqueta
        }
        cursor.close()
        return nombre
    }

}
