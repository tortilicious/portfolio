package org.mlc.gastosapi.dto.grupo

import org.mlc.gastosapi.dto.usuario.RespuestaUsuario
import org.mlc.gastosapi.model.RolGrupo

/**
 * DTO que representa a un miembro de un grupo, incluyendo su rol.
 *
 * @property usuario Los datos públicos del usuario.
 * @property rol El rol que el usuario tiene en el grupo.
 */
data class RespuestaMiembroGrupo(
    val usuario: RespuestaUsuario,
    val rol: RolGrupo
)