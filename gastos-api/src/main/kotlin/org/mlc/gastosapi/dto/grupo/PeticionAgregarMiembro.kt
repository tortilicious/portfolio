package org.mlc.gastosapi.dto.grupo

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * DTO (Data Transfer Object) para la petición de añadir un nuevo miembro a un grupo.
 * Contiene los datos que se esperan en el cuerpo de una petición para esta operación,
 * incluyendo las validaciones correspondientes para asegurar la calidad de los datos de entrada.
 *
 * @property email El email del usuario que se quiere añadir al grupo.
 * No puede estar en blanco y debe tener un formato de email válido.
 */
data class PeticionAgregarMiembro(
    @field:NotBlank(message = "El email no puede estar vacío.")
    @field:Email(message = "El formato del email no es válido.")
    val email: String,
)