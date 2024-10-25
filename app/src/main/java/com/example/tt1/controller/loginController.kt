package com.example.tt1.controller

import com.example.tt1.model.entidades.Usuario
import com.example.tt1.model.repositorios.UsuarioRepository

class UsuarioController(private val usuarioRepo: UsuarioRepository) {

    // Método para autenticar un usuario
    fun authenticate(correoE: String, contraseña: String): Int? {
        // Llama al método authenticate del repositorio
        return usuarioRepo.authenticate(correoE, contraseña)
    }

    // Método para agregar un nuevo usuario
    fun agregarUsuario(nombre: String, correoE: String, contraseña: String) {
        // Crea una nueva instancia de Usuario
        val usuario = Usuario(nUsuario = nombre, correoE = correoE, password = contraseña)
        // Llama al método agregarUsuario del repositorio
        usuarioRepo.agregarUsuario(usuario)
    }


    // Método para obtener todos los usuarios (opcional)
    fun obtenerUsuarios(): List<Usuario> {
        return usuarioRepo.obtenerUsuarios()
    }
}
