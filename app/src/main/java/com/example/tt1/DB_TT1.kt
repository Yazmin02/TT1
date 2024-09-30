package com.example.tt1

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
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Usuario (nUsuario VARCHAR NOT NULL, correoE VARCHAR NOT NULL, contraseña VARCHAR NOT NULL, idUsuario INTEGER NOT NULL PRIMARY KEY);")
        db.execSQL("CREATE TABLE IF NOT EXISTS Colaboradores (idUsuario INTEGER NOT NULL, idTareaC INTEGER NOT NULL, PRIMARY KEY (idUsuario, idTareaC), FOREIGN KEY (idTareaC) REFERENCES TareaColab (idTareaC), FOREIGN KEY (idUsuario) REFERENCES Usuario (idUsuario));")
        db.execSQL("CREATE TABLE IF NOT EXISTS Etiqueta (idEtiqueta INTEGER NOT NULL PRIMARY KEY, nombre CHAR NOT NULL);")
        db.execSQL("CREATE TABLE IF NOT EXISTS Evento (idEvento INTEGER NOT NULL PRIMARY KEY, titulo VARCHAR NOT NULL, descripcion TEXT, fInicio DATE NOT NULL, fVencimiento DATE NOT NULL, lugar VARCHAR NOT NULL);")
        db.execSQL("CREATE TABLE IF NOT EXISTS Logro (idLogro INTEGER NOT NULL PRIMARY KEY, Nombre BIGINT NOT NULL);")
        db.execSQL("CREATE TABLE IF NOT EXISTS Miembros (idUsuario INTEGER NOT NULL, idEvento INTEGER NOT NULL, PRIMARY KEY (idUsuario, idEvento), FOREIGN KEY (idEvento) REFERENCES Evento (idEvento));")
        db.execSQL("CREATE TABLE IF NOT EXISTS Recompensa (idRecompensa INTEGER NOT NULL PRIMARY KEY, puntos INTEGER NOT NULL, IdLogro INTEGER NOT NULL, idUsuario INTEGER NOT NULL, FOREIGN KEY (IdLogro) REFERENCES Logro (idLogro), FOREIGN KEY (idUsuario) REFERENCES Usuario (idUsuario));")
        db.execSQL("CREATE TABLE IF NOT EXISTS Tarea (idTarea INTEGER NOT NULL PRIMARY KEY, titulo VARCHAR NOT NULL, descripcion TEXT, fInicio DATE NOT NULL, fVencimiento DATE NOT NULL, idUsuario INTEGER NOT NULL, idEtiqueta INTEGER NOT NULL, FOREIGN KEY (idEtiqueta) REFERENCES Etiqueta (idEtiqueta));")
        db.execSQL("CREATE TABLE IF NOT EXISTS TareaColab (idTareaC INTEGER NOT NULL PRIMARY KEY, titulo VARCHAR NOT NULL, descripcion TEXT, fInicio DATE NOT NULL, fVencimiento DATE NOT NULL, etiqueta CHAR NOT NULL, idColab INTEGER NOT NULL);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Aquí puedes manejar actualizaciones de la base de datos si es necesario
        db.execSQL("DROP TABLE IF EXISTS Usuario")
        db.execSQL("DROP TABLE IF EXISTS Colaboradores")
        db.execSQL("DROP TABLE IF EXISTS Etiqueta")
        db.execSQL("DROP TABLE IF EXISTS Evento")
        db.execSQL("DROP TABLE IF EXISTS Logro")
        db.execSQL("DROP TABLE IF EXISTS Miembros")
        db.execSQL("DROP TABLE IF EXISTS Recompensa")
        db.execSQL("DROP TABLE IF EXISTS Tarea")
        db.execSQL("DROP TABLE IF EXISTS TareaColab")
        onCreate(db)
    }
}
