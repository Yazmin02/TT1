package com.example.tt1.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TareaRepository(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DB_TT1.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_TAREA = "Tarea"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Aquí puedes crear tus tablas si es necesario
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Aquí puedes manejar actualizaciones de la base de datos si es necesario
    }

    // Crear tarea
    fun agregarTarea(tarea: Tarea) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("titulo", tarea.titulo)
            put("descripcion", tarea.descripcion)
            put("fInicio", tarea.fechaInicio)
            put("fVencimiento", tarea.fechaVencimiento)
            put("idUsuario", tarea.idUsuario)
            put("idEtiqueta", tarea.idEtiqueta)
        }
        db.insert(TABLE_TAREA, null, contentValues)
        db.close()
    }

    // Leer todas las tareas
    @SuppressLint("Range")
    fun obtenerTareas(): List<Tarea> {
        val tareas = mutableListOf<Tarea>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TAREA", null)

        if (cursor.moveToFirst()) {
            do {
                val idTarea = cursor.getInt(cursor.getColumnIndex("idTarea"))
                val titulo = cursor.getString(cursor.getColumnIndex("titulo"))
                val descripcion = cursor.getString(cursor.getColumnIndex("descripcion"))
                val fInicio = cursor.getString(cursor.getColumnIndex("fInicio"))
                val fVencimiento = cursor.getString(cursor.getColumnIndex("fVencimiento"))
                val idUsuario = cursor.getInt(cursor.getColumnIndex("idUsuario"))
                val idEtiqueta = cursor.getInt(cursor.getColumnIndex("idEtiqueta"))

                tareas.add(Tarea(idTarea, titulo, descripcion, fInicio, fVencimiento, idUsuario, idEtiqueta))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tareas
    }

    // Actualizar tarea
    fun actualizarTarea(tarea: Tarea) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("titulo", tarea.titulo)
            put("descripcion", tarea.descripcion)
            put("fInicio", tarea.fechaInicio)
            put("fVencimiento", tarea.fechaVencimiento)
            put("idUsuario", tarea.idUsuario)
            put("idEtiqueta", tarea.idEtiqueta)
        }
        db.update(TABLE_TAREA, contentValues, "idTarea = ?", arrayOf(tarea.idTarea.toString()))
        db.close()
    }

    // Eliminar tarea
    fun eliminarTarea(idTarea: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_TAREA, "idTarea = ?", arrayOf(idTarea.toString()))
        db.close()
    }
}
