package com.example.tt1.model

class UsuarioRepository {

    private val usuarios: MutableList<Usuario> = mutableListOf()

    fun agregarUsuario(usuario: Usuario) {
        usuarios.add(usuario)
    }

    fun obtenerUsuarios(): List<Usuario> {
        return usuarios
    }

    fun actualizarUsuario(usuario: Usuario) {
        val index = usuarios.indexOfFirst { it.idUsuario == usuario.idUsuario }
        if (index != -1) {
            usuarios[index] = usuario
        }
    }

    fun eliminarUsuario(idUsuario: Int) {
        usuarios.removeIf { it.idUsuario == idUsuario }
    }

    fun authenticate(email: String, password: String): Int? {
        // Busca un usuario que coincida con el email y la contraseña
        for (usuario in usuarios) {
            if (usuario.correoE == email && usuario.contraseña == password) {
                return usuario.idUsuario // Devuelve el ID del usuario
            }
        }
        return null // Si no se encontró, retorna null
    }
}
