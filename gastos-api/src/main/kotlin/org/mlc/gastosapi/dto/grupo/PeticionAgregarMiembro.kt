package org.mlc.gastosapi.dto.grupo

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class PeticionAgregarMiembro(
    @field:NotEmpty(message = "El email no puede estar vacío.")
    @field:Email
    val email: String,
)
