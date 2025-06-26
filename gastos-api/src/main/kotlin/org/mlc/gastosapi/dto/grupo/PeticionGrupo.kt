package org.mlc.gastosapi.dto.grupo

import jakarta.validation.constraints.NotBlank

/**
 * DTO para la petición de creación de un nuevo grupo.
 * @property nombre El nombre del nuevo grupo.
 * @property descripcion Una descripción opcional para el grupo.
 */
data class PeticionGrupo(
    @field:NotBlank(message = "El nombre del grupo no puede estar vacío.")
    val nombre: String,
    val descripcion: String?
)
