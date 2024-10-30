package com.example.tt1.model.repositorios

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.tt1.DatabaseHelper
import com.example.tt1.model.entidades.Tarea

class TareaRepository(private val dbHelper: DatabaseHelper) {

    fun agregarTarea(tarea: Tarea) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("titulo", tarea.titulo)
            put("descripcion", tarea.descripcion)
            put("fInicio", tarea.fInicio)
            put("fVencimiento", tarea.fVencimiento)
            put("idEtiqueta", tarea.idEtiqueta)
            put("estado", tarea.estado) // Asegúrate de incluir el estado
        }

        val newRowId = db.insert("Tarea", null, values)
        if (newRowId == -1L) {
            Log.e("TareaRepository", "Error al insertar la tarea: ${tarea.titulo}")
            throw Exception("Error al insertar la tarea")
        } else {
            Log.d("TareaRepository", "Tarea insertada con éxito: ID = $newRowId")
        }

        db.close()
    }

    @SuppressLint("Range")
    fun obtenerTareas(): List<Tarea> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query("Tarea", null, null, null, null, null, null)
        val tareas = mutableListOf<Tarea>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("idTarea"))
            val titulo = cursor.getString(cursor.getColumnIndex("titulo"))
            val descripcion = cursor.getString(cursor.getColumnIndex("descripcion"))
            val fInicio = cursor.getString(cursor.getColumnIndex("fInicio"))
            val fVencimiento = cursor.getString(cursor.getColumnIndex("fVencimiento"))
            val idEtiqueta = cursor.getInt(cursor.getColumnIndex("idEtiqueta"))
            val estado = cursor.getInt(cursor.getColumnIndex("estado")) // Obteniendo el estado

            val tarea = Tarea(id, titulo, descripcion, fInicio, fVencimiento, estado, idEtiqueta, idUsuario = 1)
            tareas.add(tarea)
        }

        cursor.close()
        db.close()
        return tareas
    }

    @SuppressLint("Range")
    fun obtenerTareaPorId(id: Int): Tarea? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query("Tarea", null, "idTarea=?", arrayOf(id.toString()), null, null, null)

        return if (cursor.moveToFirst()) {
            val titulo = cursor.getString(cursor.getColumnIndex("titulo"))
            val descripcion = cursor.getString(cursor.getColumnIndex("descripcion"))
            val fInicio = cursor.getString(cursor.getColumnIndex("fInicio"))
            val fVencimiento = cursor.getString(cursor.getColumnIndex("fVencimiento"))
            val idEtiqueta = cursor.getInt(cursor.getColumnIndex("idEtiqueta"))
            val estado = cursor.getInt(cursor.getColumnIndex("estado"))

            Tarea(id, titulo, descripcion, fInicio, fVencimiento, estado, idEtiqueta, idUsuario = 1)
        } else {
            null
        }.also {
            cursor.close()
        }
    }

    fun eliminarTarea(idTarea: Int) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val deletedRows = db.delete("Tarea", "idTarea = ?", arrayOf(idTarea.toString()))

        if (deletedRows > 0) {
            Log.d("TareaRepository", "Tarea eliminada con éxito: ID = $idTarea")
        } else {
            Log.e("TareaRepository", "Error al eliminar la tarea: ID = $idTarea")
            throw Exception("Error al eliminar la tarea")
        }

        db.close()
    }


    fun actualizarTarea(tarea: Tarea) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("titulo", tarea.titulo)
            put("descripcion", tarea.descripcion)
            put("fInicio", tarea.fInicio)
            put("fVencimiento", tarea.fVencimiento)
            put("idEtiqueta", tarea.idEtiqueta)
            put("estado", tarea.estado) // Asegúrate de incluir el estado
        }

        val updatedRows = db.update("Tarea", values, "idTarea = ?", arrayOf(tarea.id.toString()))

        if (updatedRows > 0) {
            Log.d("TareaRepository", "Tarea actualizada con éxito: ID = ${tarea.id}")
        } else {
            Log.e("TareaRepository", "Error al actualizar la tarea: ID = ${tarea.id}")
            throw Exception("Error al actualizar la tarea")
        }

        db.close()
    }

    fun marcarTareaComoCompleta(id: Int) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("estado", 1) // Cambiar estado a completada
        }

        val updatedRows = db.update("Tarea", values, "idTarea = ?", arrayOf(id.toString()))

        if (updatedRows > 0) {
            Log.d("TareaRepository", "Tarea marcada como completada: ID = $id")
        } else {
            Log.e("TareaRepository", "Error al marcar la tarea como completada: ID = $id")
            throw Exception("Error al marcar la tarea como completada")
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
