package org.mlc.gastosapi.service

import org.mlc.gastosapi.dto.grupo.PeticionGrupo
import org.mlc.gastosapi.dto.grupo.RespuestaGrupo
import org.mlc.gastosapi.dto.usuario.RespuestaUsuario


interface GrupoService {
    fun crearGrupo(idUsuario: Long, peticion: PeticionGrupo): RespuestaGrupo
    fun eliminarGrupo(idGrupo: Long, idUsuario: Long)
    fun obtenerGruposUsuario(idUsuario: Long): List<RespuestaGrupo>
    fun obtenerUsuariosGrupo(idGrupo: Long): List<RespuestaUsuario>
    fun agregarUsuarioGrupo(idGrupo: Long, idUsuario: Long): RespuestaGrupo
    fun eliminarUsuarioGrupo(idGrupo: Long, idUsuario: Long)
}