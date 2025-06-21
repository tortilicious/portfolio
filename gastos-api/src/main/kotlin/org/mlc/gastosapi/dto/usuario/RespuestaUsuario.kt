package org.mlc.gastosapi.dto.usuario


/**
 * DTO para representar la información pública de un usuario.
 * @property id El ID único del usuario.
 * @property nombre El nombre del usuario.
 * @property email El email del usuario.
 */
data class RespuestaUsuario(
    val id: Long,
    val nombre: String,
    val email: String
)
