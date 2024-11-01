package com.example.tt1.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.tt1.DatabaseHelper

class RecompensaModel(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    // Obtiene los puntos totales de un usuario específico
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

    // Obtiene la lista de logros de un usuario específico
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

    // Agrega una recompensa (puntos y logro) a un usuario específico
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

    // Verifica si el usuario tiene logros simples (sin eventos)
    fun verificarLogros(idUsuario: Int) {
        if (esPrimerInicioSesion(idUsuario)) otorgarLogro(idUsuario, 1) // Logro: "Inicio de Sesión Exitoso"
        if (esPrimeraTareaCompletada(idUsuario)) otorgarLogro(idUsuario, 2) // Logro: "Primera Tarea Completada"
        if (haCreadoTresTareas(idUsuario)) otorgarLogro(idUsuario, 4) // Logro: "Creador de Tareas"
    }

    // Función para otorgar logros al usuario
    private fun otorgarLogro(idUsuario: Int, idLogro: Int) {
        // Verifica si el logro ya ha sido otorgado
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM Recompensa WHERE idUsuario = ? AND IdLogro = ?",
            arrayOf(idUsuario.toString(), idLogro.toString())
        )
        val logroExistente = cursor.moveToFirst() && cursor.getInt(0) > 0
        cursor.close()
        db.close()

        if (!logroExistente) {
            agregarRecompensa(0, idLogro, idUsuario)
        }
    }

    // Comprueba si el usuario ha iniciado sesión por primera vez
    private fun esPrimerInicioSesion(idUsuario: Int): Boolean {
        return getLogros(idUsuario).none { it == "Inicio de Sesión Exitoso" }
    }

    // Comprueba si el usuario ha completado su primera tarea
    private fun esPrimeraTareaCompletada(idUsuario: Int): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM Tarea WHERE idUsuario = ? AND estado = 1",
            arrayOf(idUsuario.toString())
        )
        val primeraTareaCompletada = cursor.moveToFirst() && cursor.getInt(0) > 0
        cursor.close()
        db.close()
        return primeraTareaCompletada
    }

    // Comprueba si el usuario ha creado tres tareas
    private fun haCreadoTresTareas(idUsuario: Int): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM Tarea WHERE idUsuario = ?",
            arrayOf(idUsuario.toString())
        )
        val tresTareasCreadas = cursor.moveToFirst() && cursor.getInt(0) >= 3
        cursor.close()
        db.close()
        return tresTareasCreadas
    }

    // Método para asignar puntos según la antelación al completar la tarea
    fun asignarPuntosPorTarea(fechaLimite: Long, fechaCompletada: Long, idUsuario: Int) {
        val diasDeAnticipacion = ((fechaLimite - fechaCompletada) / (1000 * 60 * 60 * 24)).toInt()
        val puntos = when {
            diasDeAnticipacion > 2 -> 100
            diasDeAnticipacion == 1 -> 75
            diasDeAnticipacion == 0 -> 50
            diasDeAnticipacion < 0 -> 0
            else -> -25
        }
        agregarRecompensa(puntos, 0, idUsuario) // Asigna puntos sin logro específico
    }

    // Obtiene el nivel del usuario basado en los puntos totales
    fun getNivelUsuario(puntosTotales: Int): String {
        return when (puntosTotales) {
            in 0..500 -> "Principiante en Organización"
            in 501..1000 -> "Aprendiz de Tiempo"
            in 1001..2000 -> "Organizador Junior"
            in 2001..3000 -> "Maestro del Tiempo"
            else -> "Experto en Planificación"
        }
    }
}
