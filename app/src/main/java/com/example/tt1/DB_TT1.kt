package com.example.tt1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "DB_TT1.db"
        private const val DATABASE_VERSION = 3
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """ 
        CREATE TABLE IF NOT EXISTS Usuario (
            idUsuario INTEGER PRIMARY KEY AUTOINCREMENT,
            nUsuario TEXT NOT NULL,
            correoE TEXT NOT NULL,
            contraseña TEXT NOT NULL
        ); 
    """.trimIndent()
        )

        db.execSQL(
            """ 
        CREATE TABLE IF NOT EXISTS Colaboradores (
            idUsuario INTEGER NOT NULL,
            idTareaC INTEGER NOT NULL,
            PRIMARY KEY (idUsuario, idTareaC),
            FOREIGN KEY (idTareaC) REFERENCES TareaColab (idTareaC),
            FOREIGN KEY (idUsuario) REFERENCES Usuario (idUsuario)
        ); 
    """.trimIndent()
        )

        db.execSQL(
            """ 
        CREATE TABLE IF NOT EXISTS Etiqueta (
            idEtiqueta INTEGER PRIMARY KEY,
            nombre TEXT NOT NULL
        ); 
    """.trimIndent()
        )

        insertarEtiquetas(db)

        db.execSQL(
            """ 
        CREATE TABLE IF NOT EXISTS Evento (
            idEvento INTEGER PRIMARY KEY AUTOINCREMENT,
            titulo TEXT NOT NULL,
            descripcion TEXT,
            fInicio DATE NOT NULL,
            fVencimiento DATE NOT NULL,
            lugar TEXT NOT NULL,
            estado INTEGER NOT NULL DEFAULT 0,
            idEtiqueta INTEGER NOT NULL,
            FOREIGN KEY (idEtiqueta) REFERENCES Etiqueta (idEtiqueta)
        ); 
    """.trimIndent()
        )

        db.execSQL(
            """ 
        CREATE TABLE IF NOT EXISTS Logro (
            idLogro INTEGER PRIMARY KEY AUTOINCREMENT,
            Nombre TEXT NOT NULL
        ); 
    """.trimIndent()
        )

        db.execSQL(
            """ 
        CREATE TABLE IF NOT EXISTS Miembros (
            idUsuario INTEGER NOT NULL,
            idEvento INTEGER NOT NULL,
            PRIMARY KEY (idUsuario, idEvento),
            FOREIGN KEY (idEvento) REFERENCES Evento (idEvento)
        ); 
    """.trimIndent()
        )

        db.execSQL(
            """ 
        CREATE TABLE IF NOT EXISTS Recompensa (
            idRecompensa INTEGER PRIMARY KEY AUTOINCREMENT,
            puntos INTEGER NOT NULL,
            IdLogro INTEGER NOT NULL,
            idUsuario INTEGER NOT NULL,
            FOREIGN KEY (IdLogro) REFERENCES Logro (idLogro),
            FOREIGN KEY (idUsuario) REFERENCES Usuario (idUsuario)
        ); 
    """.trimIndent()
        )

        db.execSQL(
            """ 
        CREATE TABLE IF NOT EXISTS Tarea (
            idTarea INTEGER PRIMARY KEY AUTOINCREMENT,
            titulo TEXT NOT NULL,
            descripcion TEXT,
            fInicio DATE NOT NULL,
            fVencimiento DATE NOT NULL,
            estado INTEGER NOT NULL DEFAULT 0,
            idUsuario INTEGER NOT NULL,
            idEtiqueta INTEGER NOT NULL,
            FOREIGN KEY (idEtiqueta) REFERENCES Etiqueta (idEtiqueta)
            
        ); 
        
    """.trimIndent()

        )


        db.execSQL(
            """ 
        CREATE TABLE IF NOT EXISTS TareaColab (
            idTareaC INTEGER PRIMARY KEY AUTOINCREMENT,
            titulo TEXT NOT NULL,
            descripcion TEXT,
            fInicio DATE NOT NULL,
            fVencimiento DATE NOT NULL,
            etiqueta TEXT NOT NULL,
            idColab INTEGER NOT NULL
        ); 
    """.trimIndent()
        )
        insertarLogros(db)  // Llamada para insertar logros iniciales

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    private fun insertarEtiquetas(db: SQLiteDatabase) {
        // Inserciones manuales con IDs específicos
        val etiquetas = listOf(
            Pair(1, "Escuela"),
            Pair(2, "Trabajo"),
            Pair(3, "Ocio")
        )

        db.beginTransaction()
        try {
            for (etiqueta in etiquetas) {
                val contentValues = ContentValues().apply {
                    put("idEtiqueta", etiqueta.first) // ID específico
                    put("nombre", etiqueta.second)      // Nombre de la etiqueta
                }
                db.insert("Etiqueta", null, contentValues)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }
    private fun insertarLogros(db: SQLiteDatabase) {
        val logros = listOf(
            Pair(1, "Inicio de Sesión Exitoso"),
            Pair(2, "Primera Tarea Completada"),
            Pair(3, "Primer Evento Completado"),
            Pair(4, "Creador de Tareas"), // Por crear tres tareas
            Pair(5, "Organizador de Eventos") // Por crear tres eventos
        )

        db.beginTransaction()
        try {
            for (logro in logros) {
                val contentValues = ContentValues().apply {
                    put("idLogro", logro.first) // ID específico
                    put("Nombre", logro.second) // Nombre del logro
                }
                db.insert("Logro", null, contentValues)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }
}