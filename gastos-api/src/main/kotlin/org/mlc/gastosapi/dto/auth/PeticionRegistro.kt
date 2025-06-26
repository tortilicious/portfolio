package org.mlc.gastosapi.dto.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


/**
 * DTO para la petición de registro de un nuevo usuario.
 * @property nombre El nombre del nuevo usuario.
 * @property email El email del nuevo usuario, se usará para el login.
 * @property password La contraseña elegida por el usuario.
 */
data class PeticionRegistro(
    @field:NotBlank(message = "El nombre no puede estar vacío.")
    @field:Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
    val nombre: String,

    @field:NotBlank(message = "El email no puede estar vacío.")
    @field:Email(message = "El formato del email no es válido.")
    val email: String,

    @field:NotBlank(message = "La contraseña no puede estar vacía.")
    @field:Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    val password: String
)
