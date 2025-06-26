package org.mlc.gastosapi.service

import org.mlc.gastosapi.dto.grupo.PeticionGrupo
import org.mlc.gastosapi.dto.grupo.RespuestaGrupo
import org.mlc.gastosapi.model.RolGrupo


interface GrupoService {
    fun crearGrupo(idUsuarioActual: Long, peticion: PeticionGrupo): RespuestaGrupo
    fun eliminarGrupo(idGrupo: Long, idUsuarioActual: Long)
    fun obtenerGruposUsuario(idUsuarioActual: Long): List<RespuestaGrupo>
    fun obtenerGrupo(idUsuarioActual: Long, idGrupo: Long): RespuestaGrupo
    fun agregarMiembro(idGrupo: Long, idUsuarioActual: Long, emailUsuarioAAgregar: String): RespuestaGrupo
    fun eliminarMiembro(idGrupo: Long, idUsuarioActual: Long, idUsuarioAEliminar: Long)
    fun actualizarRolMiembro(idGrupo: Long, idUsuarioActual: Long, idUsuarioAActualizar: Long, nuevoRol: RolGrupo): RespuestaGrupo
}