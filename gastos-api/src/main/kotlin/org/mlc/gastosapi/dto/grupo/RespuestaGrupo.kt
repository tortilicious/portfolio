package org.mlc.gastosapi.dto.grupo

import org.mlc.gastosapi.dto.usuario.RespuestaUsuario


/**
 * DTO para representar la información detallada de un grupo en las respuestas de la API.
 *
 * @property id El ID del grupo.
 * @property nombre El nombre del grupo.
 * @property descripcion La descripción del grupo.
 * @property creador Un resumen del usuario que creó originalmente el grupo.
 * @property miembros Una lista de los miembros actuales del grupo, incluyendo sus roles.
 */
data class RespuestaGrupo(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val creador: RespuestaUsuario,
    val miembros: List<RespuestaMiembroGrupo>
)
