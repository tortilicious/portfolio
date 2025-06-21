package org.mlc.gastosapi.dto.auth


/**
 * DTO para la petición de inicio de sesión.
 * @property email El email del usuario.
 * @property password La contraseña del usuario.
 */
data class PeticionLogin(
    val email: String,
    val password: String
)
