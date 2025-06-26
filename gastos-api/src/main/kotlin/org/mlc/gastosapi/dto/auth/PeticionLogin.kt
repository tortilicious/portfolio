package org.mlc.gastosapi.dto.auth

import jakarta.validation.constraints.NotBlank


/**
 * DTO para la petición de inicio de sesión.
 * @property email El email del usuario.
 * @property password La contraseña del usuario.
 */
data class PeticionLogin(
    @field:NotBlank(message = "El email no puede estar vacío.")
    val email: String,
    @field:NotBlank(message = "La contraseña no puede estar vacía.")
    val password: String
)
