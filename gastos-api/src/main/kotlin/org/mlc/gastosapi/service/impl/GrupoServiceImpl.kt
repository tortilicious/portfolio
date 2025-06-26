package org.mlc.gastosapi.service.impl

import jakarta.persistence.EntityNotFoundException
import org.mlc.gastosapi.dto.grupo.PeticionGrupo
import org.mlc.gastosapi.dto.grupo.RespuestaGrupo
import org.mlc.gastosapi.utils.* // Asegúrate de que tu importación para los mappers es correcta.
import org.mlc.gastosapi.model.Grupo
import org.mlc.gastosapi.model.Membresia
import org.mlc.gastosapi.model.RolGrupo
import org.mlc.gastosapi.repository.GrupoRepository
import org.mlc.gastosapi.repository.MembresiaRepository
import org.mlc.gastosapi.repository.UsuarioRepository
import org.mlc.gastosapi.service.GrupoService
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
     * @param idUsuarioActual El ID del usuario que está creando el grupo.
     * @param peticion DTO con los datos del nuevo grupo (nombre, descripción).
     * @return El DTO del grupo recién creado.
     * @throws EntityNotFoundException si el usuario creador no existe.
     */
    override fun crearGrupo(idUsuarioActual: Long, peticion: PeticionGrupo): RespuestaGrupo {
        val creador = usuarioRepository.findById(idUsuarioActual).orElseThrow {
            EntityNotFoundException("El usuario con ID $idUsuarioActual no existe.")
        }

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
     * Elimina un grupo completo.
     * Solo el usuario que creó originalmente el grupo tiene permiso para eliminarlo.
     *
     * @param idGrupo El ID del grupo a eliminar.
     * @param idUsuarioActual El ID del usuario que realiza la operación.
     * @throws EntityNotFoundException si el grupo no existe.
     * @throws AccessDeniedException si el usuario no es el creador del grupo.
     */
    override fun eliminarGrupo(idGrupo: Long, idUsuarioActual: Long) {
        val grupo = grupoRepository.findById(idGrupo).orElseThrow {
            EntityNotFoundException("El grupo con ID $idGrupo no existe.")
        }
        if (grupo.creador.id != idUsuarioActual) {
            throw AccessDeniedException("No tienes permiso para eliminar este grupo. Solo el creador puede hacerlo.")
        }
        grupoRepository.deleteById(idGrupo)
    }

    /**
     * Obtiene una lista de todos los grupos a los que pertenece un usuario.
     *
     * @param idUsuarioActual El ID del usuario.
     * @return Una lista de [RespuestaGrupo] con los datos de los grupos.
     * @throws EntityNotFoundException si el usuario no existe.
     */
    @Transactional(readOnly = true)
    override fun obtenerGruposUsuario(idUsuarioActual: Long): List<RespuestaGrupo> {
        val usuario = usuarioRepository.findById(idUsuarioActual).orElseThrow {
            EntityNotFoundException("El usuario con ID $idUsuarioActual no existe.")
        }
        return usuario.pertenencias.map { it.grupo.dto() }
    }

    /**
     * Obtiene los detalles de un único grupo por su ID, siempre que el usuario sea miembro.
     *
     * @param idGrupo El ID del grupo a obtener.
     * @param idGrupo El ID del usuario que solicita la información.
     * @return El DTO del grupo encontrado.
     * @throws AccessDeniedException si el usuario no es miembro del grupo.
     */
    @Transactional(readOnly = true)
    override fun obtenerGrupo(idUsuarioActual: Long, idGrupo: Long): RespuestaGrupo {
        val membresia = verificarMiembro(idGrupo, idGrupo)
        return membresia.grupo.dto()
    }

    /**
     * Agrega un nuevo miembro a un grupo existente.
     * Solo un administrador del grupo puede realizar esta acción.
     *
     * @param idGrupo El ID del grupo.
     * @param idUsuarioActual El ID del usuario que realiza la acción (debe ser admin).
     * @param emailUsuarioAAgregar El email del usuario que será agregado al grupo.
     * @return El DTO del grupo actualizado con el nuevo miembro.
     * @throws EntityNotFoundException si el grupo o el usuario a agregar no existen.
     * @throws IllegalStateException si el usuario ya es miembro del grupo.
     * @throws AccessDeniedException si el usuario actual no es admin.
     */
    override fun agregarMiembro(idGrupo: Long, idUsuarioActual: Long, emailUsuarioAAgregar: String): RespuestaGrupo {
        verificarAdmin(idGrupo, idUsuarioActual)

        val grupo = grupoRepository.findById(idGrupo).get()
        val usuarioAAgregar = usuarioRepository.findByEmail(emailUsuarioAAgregar)
            ?: throw EntityNotFoundException("No se encontró un usuario con el email: $emailUsuarioAAgregar")

        if (grupo.miembros.any { it.usuario.id == usuarioAAgregar.id }) {
            throw IllegalStateException("El usuario '${usuarioAAgregar.nombre}' ya es miembro de este grupo.")
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
     * Solo un administrador puede realizar esta acción. No se puede eliminar al creador del grupo.
     *
     * @param idGrupo El ID del grupo.
     * @param idUsuarioActual El ID del usuario que realiza la acción (debe ser admin).
     * @param idUsuarioAEliminar El ID del usuario que será eliminado.
     * @throws IllegalStateException si se intenta eliminar al creador original o a uno mismo.
     * @throws EntityNotFoundException si la membresía a eliminar no existe.
     * @throws AccessDeniedException si el usuario actual no es admin.
     */
    override fun eliminarMiembro(idGrupo: Long, idUsuarioActual: Long, idUsuarioAEliminar: Long) {
        verificarAdmin(idGrupo, idUsuarioActual)

        if (idUsuarioActual == idUsuarioAEliminar) {
            throw IllegalStateException("Un administrador no puede eliminarse a sí mismo del grupo.")
        }

        val membresiaAEliminar = membresiaRepository.findByGrupo_IdAndUsuario_Id(idGrupo, idUsuarioAEliminar)
            ?: throw EntityNotFoundException("El usuario con ID $idUsuarioAEliminar no es miembro de este grupo.")

        if (membresiaAEliminar.usuario.id == membresiaAEliminar.grupo.creador.id) {
            throw IllegalStateException("No se puede eliminar al creador original del grupo.")
        }

        membresiaRepository.delete(membresiaAEliminar)
    }

    /**
     * Actualiza el rol de un miembro dentro de un grupo.
     * Solo un administrador puede realizar esta acción. No se puede cambiar el rol del creador.
     *
     * @param idGrupo El ID del grupo.
     * @param idUsuarioActual El ID del usuario que realiza la acción (debe ser admin).
     * @param idUsuarioAActualizar El ID del miembro cuyo rol se actualizará.
     * @param nuevoRol El nuevo rol a asignar.
     * @return El DTO del grupo actualizado.
     */
    override fun actualizarRolMiembro(idGrupo: Long, idUsuarioActual: Long, idUsuarioAActualizar: Long, nuevoRol: RolGrupo): RespuestaGrupo {
        verificarAdmin(idGrupo, idUsuarioActual)

        val membresiaAActualizar = membresiaRepository.findByGrupo_IdAndUsuario_Id(idGrupo, idUsuarioAActualizar)
            ?: throw EntityNotFoundException("El usuario con ID $idUsuarioAActualizar no es miembro de este grupo.")

        if (membresiaAActualizar.usuario.id == membresiaAActualizar.grupo.creador.id) {
            throw IllegalStateException("No se puede cambiar el rol del creador original del grupo.")
        }

        membresiaAActualizar.rol = nuevoRol
        membresiaRepository.save(membresiaAActualizar)
        return membresiaAActualizar.grupo.dto()
    }

    // --- FUNCIONES DE AYUDA PRIVADAS ---

    /**
     * Verifica que un usuario es miembro de un grupo.
     * @return La entidad [Membresia] si el usuario es miembro.
     * @throws AccessDeniedException si el usuario no es miembro.
     */
    private fun verificarMiembro(idGrupo: Long, idUsuario: Long): Membresia {
        return membresiaRepository.findByGrupo_IdAndUsuario_Id(idGrupo, idUsuario)
            ?: throw AccessDeniedException("Acceso denegado. No eres miembro del grupo con ID $idGrupo.")
    }

    /**
     * Verifica que un usuario es administrador de un grupo.
     * @throws AccessDeniedException si el usuario no es miembro o no es administrador.
     */
    private fun verificarAdmin(idGrupo: Long, idUsuario: Long) {
        val membresia = verificarMiembro(idGrupo, idUsuario)
        if (membresia.rol != RolGrupo.ADMIN) {
            throw AccessDeniedException("No tienes permisos de administrador para realizar esta acción.")
        }
    }
}
