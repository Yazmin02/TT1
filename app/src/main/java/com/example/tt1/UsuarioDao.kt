package com.example.tt1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tt1.model.Usuario

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario)

    @Query("SELECT * FROM Usuario WHERE email = :email AND password = :password LIMIT 1")
    suspend fun authenticate(email: String, password: String): Usuario?
}
