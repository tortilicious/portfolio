package org.mlc.gastosapi.service.impl

import jakarta.persistence.EntityNotFoundException
import org.mlc.gastosapi.dto.grupo.PeticionGrupo
import org.mlc.gastosapi.dto.grupo.RespuestaGrupo
import org.mlc.gastosapi.dto.usuario.RespuestaUsuario
import org.mlc.gastosapi.model.Grupo
import org.mlc.gastosapi.model.Membresia
import org.mlc.gastosapi.model.RolGrupo
import org.mlc.gastosapi.repository.GrupoRepository
import org.mlc.gastosapi.repository.MembresiaRepository
import org.mlc.gastosapi.repository.UsuarioRepository
import org.mlc.gastosapi.service.GrupoService
import org.mlc.gastosapi.utils.dto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.AccessDeniedException

/**
 * Implementación del servicio para la gestión de la lógica de negocio de los Grupos.
 *
 * @property grupoRepository Repositorio para acceder a los datos de los Grupos.
 * @property usuarioRepository Repositorio para acceder a los datos de los Usuarios.
 * @property membresiaRepository Repositorio para gestionar las relaciones entre usuarios y grupos.
 */
@Service
@Transactional
class GrupoServiceImpl(
    private val grupoRepository: GrupoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val membresiaRepository: MembresiaRepository
) : GrupoService {

    /**
     * Crea un nuevo grupo y asigna al usuario creador como primer miembro con rol ADMIN.
     *
     * @param idUsuario El ID del usuario que está creando el grupo.
     * @param peticion DTO con los datos del nuevo grupo (nombre, descripción).
     * @return El DTO del grupo recién creado.
     * @throws EntityNotFoundException si el usuario creador no existe.
     */
    override fun crearGrupo(idUsuario: Long, peticion: PeticionGrupo): RespuestaGrupo {
        val creador = usuarioRepository.findByIdOrNull(idUsuario)
            ?: throw EntityNotFoundException("El usuario con ID $idUsuario no existe.")

        val nuevoGrupo = Grupo(
            nombre = peticion.nombre,
            descripcion = peticion.descripcion,
            creador = creador
        )

        val membresiaCreador = Membresia(
            usuario = creador,
            grupo = nuevoGrupo,
            rol = RolGrupo.ADMIN
        )

        nuevoGrupo.miembros.add(membresiaCreador)

        val grupoGuardado = grupoRepository.save(nuevoGrupo)

        return grupoGuardado.dto()
    }

    /**
     * Elimina un grupo, previa verificación de que el usuario actual es administrador.
     *
     * @param idGrupo El ID del grupo a eliminar.
     * @param idUsuario El ID del usuario que realiza la operación.
     * @throws EntityNotFoundException si el grupo no existe.
     * @throws AccessDeniedException si el usuario no tiene permisos de administrador.
     */
    override fun eliminarGrupo(idGrupo: Long, idUsuario: Long) {
        verificarAdmin(idGrupo, idUsuario)

        if (!grupoRepository.existsById(idGrupo)) {
            throw EntityNotFoundException("El grupo con ID $idGrupo no existe.")
        }

        grupoRepository.deleteById(idGrupo)
    }

    /**
     * Obtiene una lista de todos los grupos a los que pertenece un usuario.
     *
     * @param idUsuario El ID del usuario.
     * @return Una lista de [RespuestaGrupo] con los datos de los grupos.
     * @throws EntityNotFoundException si el usuario no existe.
     */
    @Transactional(readOnly = true)
    override fun obtenerGruposUsuario(idUsuario: Long): List<RespuestaGrupo> {
        val usuario = usuarioRepository.findByIdOrNull(idUsuario)
            ?: throw EntityNotFoundException("El usuario con ID $idUsuario no existe.")

        return usuario.pertenencias.map { it.grupo.dto() }
    }

    /**
     * Obtiene una lista de todos los usuarios que son miembros de un grupo específico.
     *
     * @param idGrupo El ID del grupo.
     * @return Una lista de [RespuestaUsuario] con los datos de los miembros.
     * @throws EntityNotFoundException si el grupo no existe.
     */
    @Transactional(readOnly = true)
    override fun obtenerMiembrosGrupo(idGrupo: Long): List<RespuestaUsuario> {
        val grupo = grupoRepository.findByIdOrNull(idGrupo)
            ?: throw EntityNotFoundException("El grupo con ID $idGrupo no existe.")

        return grupo.miembros.map { it.usuario.dto() }
    }

    /**
     * Agrega un nuevo miembro a un grupo existente.
     *
     * @param idGrupo El ID del grupo al que se agregará el miembro.
     * @param idUsuarioActual El ID del usuario que realiza la acción (debe ser admin).
     * @param idUsuarioAgregado El ID del usuario que será agregado al grupo.
     * @return El DTO del grupo actualizado con el nuevo miembro.
     * @throws EntityNotFoundException si el grupo o el usuario a agregar no existen.
     * @throws IllegalStateException si el usuario ya es miembro del grupo.
     */
    override fun agregarMiembroGrupo(idGrupo: Long, idUsuarioActual: Long, idUsuarioAgregado: Long): RespuestaGrupo {
        verificarAdmin(idGrupo, idUsuarioActual)

        val grupo = grupoRepository.findByIdOrNull(idGrupo)
            ?: throw EntityNotFoundException("El grupo con ID $idGrupo no existe.")

        val usuarioAAgregar = usuarioRepository.findByIdOrNull(idUsuarioAgregado)
            ?: throw EntityNotFoundException("El usuario a agregar con ID $idUsuarioAgregado no existe.")

        if (grupo.miembros.any { it.usuario.id == idUsuarioAgregado }) {
            throw IllegalStateException("El usuario con ID $idUsuarioAgregado ya es miembro de este grupo.")
        }

        val nuevaMembresia = Membresia(
            usuario = usuarioAAgregar,
            grupo = grupo,
            rol = RolGrupo.MEMBER
        )

        grupo.miembros.add(nuevaMembresia)
        val grupoActualizado = grupoRepository.save(grupo)

        return grupoActualizado.dto()
    }

    /**
     * Elimina a un miembro de un grupo.
     *
     * @param idGrupo El ID del grupo.
     * @param idUsuarioActual El ID del usuario que realiza la acción (debe ser admin).
     * @param idUsuarioEliminado El ID del usuario que será eliminado.
     * @throws IllegalStateException si se intenta eliminar al creador original del grupo.
     */
    override fun eliminarMiembroGrupo(idGrupo: Long, idUsuarioActual: Long, idUsuarioEliminado: Long) {
        verificarAdmin(idGrupo, idUsuarioActual)

        val membresiaAEliminar = membresiaRepository.findByGrupo_IdAndUsuario_Id(idGrupo, idUsuarioEliminado)
            ?: throw EntityNotFoundException("El usuario con id $idUsuarioEliminado no es miembro de este grupo.")

        if (membresiaAEliminar.usuario.id == membresiaAEliminar.grupo.creador.id) {
            throw IllegalStateException("No se puede eliminar al creador original del grupo.")
        }

        membresiaRepository.delete(membresiaAEliminar)
    }

    /**
     * Función privada para verificar si un usuario tiene el rol de ADMIN en un grupo.
     * Centraliza la lógica de autorización.
     *
     * @param idGrupo El ID del grupo a verificar.
     * @param idUsuario El ID del usuario a verificar.
     * @throws AccessDeniedException si el usuario no es miembro o no es administrador.
     */
    private fun verificarAdmin(idGrupo: Long, idUsuario: Long) {
        val membresia = membresiaRepository.findByGrupo_IdAndUsuario_Id(idGrupo, idUsuario)
            ?: throw AccessDeniedException("Acceso denegado. No eres miembro de este grupo")

        if (membresia.rol != RolGrupo.ADMIN) {
            throw AccessDeniedException("No tienes permisos para realizar esta acción.")
        }
    }
}