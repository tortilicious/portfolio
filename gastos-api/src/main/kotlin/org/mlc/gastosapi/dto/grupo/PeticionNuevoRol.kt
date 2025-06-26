package org.mlc.gastosapi.dto.grupo

import jakarta.validation.constraints.NotNull
import org.mlc.gastosapi.model.RolGrupo

/**
 * DTO (Data Transfer Object) para la petición de actualizar el rol de un miembro en un grupo.
 * Contiene el nuevo rol que se asignará, con la validación de que no puede ser nulo.
 *
 * @property nuevoRol El nuevo rol que se le asignará al miembro.
 * No puede ser nulo. Debe ser uno de los valores del enum [RolGrupo] (ej: "ADMIN" o "MEMBER").
 */
data class PeticionNuevoRol(
    @field:NotNull(message = "Se debe asignar un rol.")
    val nuevoRol: RolGrupo,
)