// TareaRepository.kt
package com.example.tt1

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.tt1.model.Tarea
import com.example.tt1.repository.TareaDao

class TareaRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)
    private val db: SQLiteDatabase = dbHelper.writableDatabase

    fun insertarTarea(tarea: Tarea) {
        val tareaDao = TareaDao(db)
        tareaDao.insertarTarea(tarea)
    }

    fun obtenerTareasPorUsuario(idUsuario: Int): List<Tarea> {
        val tareaDao = TareaDao(db)
        return tareaDao.obtenerTareasPorUsuario(idUsuario)
    }

    fun agregarTarea(tarea: Tarea) {

    }
}
