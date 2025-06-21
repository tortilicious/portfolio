package org.mlc.gastosapi.dto.auth


/**
 * DTO para la petición de registro de un nuevo usuario.
 * @property nombre El nombre del nuevo usuario.
 * @property email El email del nuevo usuario, se usará para el login.
 * @property password La contraseña elegida por el usuario.
 */
data class PeticionRegistro(
    val nombre: String,
    val email: String,
    val password: String
)
