package org.mlc.gastosapi.dto.grupo

import jakarta.validation.constraints.NotNull
import org.mlc.gastosapi.model.RolGrupo

data class PeticionNuevoRol(
    @field:NotNull(message = "Se debe asignar un rol.")
    val nuevoRol: RolGrupo,
)
