package org.mlc.gastosapi.dto.grupo

import org.mlc.gastosapi.dto.usuario.RespuestaUsuario


/**
 * DTO para representar un grupo en una lista.
 * @property id El ID del grupo.
 * @property nombre El nombre del grupo.
 * @property descripcion La descripción del grupo.
 * @property creador Un resumen del usuario que creó el grupo.
 */
data class RespuestaGrupo(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val creador: RespuestaUsuario
)
