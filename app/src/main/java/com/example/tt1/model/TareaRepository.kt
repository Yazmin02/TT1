package com.example.tt1

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.tt1.model.Tarea

class TareaRepository(private val dbHelper: DatabaseHelper) {

    fun agregarTarea(tarea: Tarea) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("titulo", tarea.titulo)
            put("descripcion", tarea.descripcion)
            put("fInicio", tarea.fInicio)
            put("fVencimiento", tarea.fVencimiento)
            put("idEtiqueta", tarea.idEtiqueta)
        }

        // Realiza la inserción y verifica el resultado
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
        val cursor: Cursor = db.query(
            "Tarea",
            null,
            null,
            null,
            null,
            null,
            null
        )
        val tareas = mutableListOf<Tarea>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("idTarea"))
            val titulo = cursor.getString(cursor.getColumnIndex("titulo"))
            val descripcion = cursor.getString(cursor.getColumnIndex("descripcion"))
            val fInicio = cursor.getString(cursor.getColumnIndex("fInicio"))
            val fVencimiento = cursor.getString(cursor.getColumnIndex("fVencimiento"))
            val idEtiqueta = cursor.getInt(cursor.getColumnIndex("idEtiqueta"))

            val tarea = Tarea(id, titulo, descripcion, fInicio, fVencimiento, idEtiqueta, idUsuario = 1)
            tareas.add(tarea)
        }

        cursor.close()
        db.close()
        return tareas
    }
}
