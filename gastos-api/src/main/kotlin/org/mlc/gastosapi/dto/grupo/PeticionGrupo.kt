package org.mlc.gastosapi.dto.grupo

/**
 * DTO para la petición de creación de un nuevo grupo.
 * @property nombre El nombre del nuevo grupo.
 * @property descripcion Una descripción opcional para el grupo.
 */
data class PeticionGrupo(
    val nombre: String,
    val descripcion: String?
)
