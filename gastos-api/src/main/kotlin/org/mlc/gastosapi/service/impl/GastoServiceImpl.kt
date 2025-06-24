package org.mlc.gastosapi.service.impl

import jakarta.persistence.EntityNotFoundException
import org.mlc.gastosapi.dto.gasto.PeticionGasto
import org.mlc.gastosapi.dto.gasto.RespuestaGasto
import org.mlc.gastosapi.model.DivisionGasto
import org.mlc.gastosapi.model.Gasto
import org.mlc.gastosapi.model.RolGrupo
import org.mlc.gastosapi.repository.GastoRepository
import org.mlc.gastosapi.repository.GrupoRepository
import org.mlc.gastosapi.repository.MembresiaRepository
import org.mlc.gastosapi.repository.UsuarioRepository
import org.mlc.gastosapi.service.GastoService
import org.mlc.gastosapi.utils.dto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/**
 * Implementación del servicio para la gestión de la lógica de negocio de los Gastos.
 *
 * @property gastoRepository Repositorio para acceder a los datos de los Gastos.
 * @property usuarioRepository Repositorio para acceder a los datos de los Usuarios.
 * @property grupoRepository Repositorio para acceder a los datos de los Grupos.
 * @property membresiaRepository Repositorio para gestionar las relaciones entre usuarios y grupos.
 */
@Service
@Transactional
class GastoServiceImpl(
    private val gastoRepository: GastoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val grupoRepository: GrupoRepository,
    private val membresiaRepository: MembresiaRepository
) : GastoService {

    /**
     * Crea un nuevo gasto dentro de un grupo, validando todos los datos y permisos.
     *
     * @param idGrupo El ID del grupo donde se crea el gasto.
     * @param idUsuarioActual El ID del usuario que realiza la petición.
     * @param peticion DTO con los detalles del gasto.
     * @return El DTO del gasto recién creado.
     * @throws EntityNotFoundException si el grupo no existe.
     * @throws AccessDeniedException si el usuario no es miembro o algún pagador/deudor no pertenece al grupo.
     * @throws IllegalArgumentException si la suma de las divisiones no coincide con el monto total.
     */
    override fun crearGasto(idGrupo: Long, idUsuarioActual: Long, peticion: PeticionGasto): RespuestaGasto {
        val grupo = grupoRepository.findByIdOrNull(idGrupo)
            ?: throw EntityNotFoundException("El grupo con ID $idGrupo no existe.")

        verificarMiembro(idGrupo, idUsuarioActual)

        val idsMiembrosGrupo = grupo.miembros.map { it.usuario.id }.toSet()

        if (peticion.pagadorId !in idsMiembrosGrupo) {
            throw AccessDeniedException("El pagador con ID ${peticion.pagadorId} no es miembro del grupo.")
        }

        peticion.divisionGasto.forEach { division ->
            if (division.deudorId !in idsMiembrosGrupo) {
                throw AccessDeniedException("El deudor con ID ${division.deudorId} no es miembro del grupo.")
            }
        }

        val sumaDivisiones = peticion.divisionGasto.map { it.montoAdeudado }.fold(BigDecimal.ZERO, BigDecimal::add)
        if (peticion.monto.compareTo(sumaDivisiones) != 0) {
            throw IllegalArgumentException("La suma de las divisiones ($sumaDivisiones) no coincide con el monto total del gasto (${peticion.monto})")
        }

        val pagador = usuarioRepository.findByIdOrNull(peticion.pagadorId)!!

        val nuevoGasto = Gasto(
            descripcion = peticion.descripcion,
            monto = peticion.monto,
            pagador = pagador,
            grupo = grupo
        )

        val divisiones = peticion.divisionGasto.map { divisionDto ->
            val deudor = usuarioRepository.findByIdOrNull(divisionDto.deudorId)!!
            DivisionGasto(
                gasto = nuevoGasto,
                deudor = deudor,
                cantidadDebida = divisionDto.montoAdeudado
            )
        }

        nuevoGasto.divisiones.addAll(divisiones)

        val gastoGuardado = gastoRepository.save(nuevoGasto)
        return gastoGuardado.dto()
    }

    /**
     * Obtiene los detalles de un gasto específico, previa validación de permisos.
     *
     * @param idGrupo El ID del grupo al que pertenece el gasto.
     * @param idUsuarioActual El ID del usuario que solicita la información.
     * @param idGasto El ID del gasto a obtener.
     * @return El DTO del gasto encontrado.
     * @throws EntityNotFoundException si el gasto no se encuentra.
     * @throws AccessDeniedException si el usuario no es miembro o el gasto no pertenece al grupo.
     */
    override fun obtenerGasto(idGrupo: Long, idUsuarioActual: Long, idGasto: Long): RespuestaGasto {
        verificarMiembro(idGrupo, idUsuarioActual)

        val gasto = gastoRepository.findByIdOrNull(idGasto)
            ?: throw EntityNotFoundException("El gasto con ID $idGasto no existe.")

        if (gasto.grupo.id != idGrupo) {
            throw AccessDeniedException("El gasto con ID $idGasto no pertenece al grupo con ID $idGrupo.")
        }

        return gasto.dto()
    }

    /**
     * Obtiene una lista con todos los gastos de un grupo específico.
     *
     * @param idGrupo El ID del grupo.
     * @param idUsuarioActual El ID del usuario que solicita la información.
     * @return Una lista de DTOs con los gastos del grupo, ordenados por fecha descendente.
     * @throws AccessDeniedException si el usuario no es miembro del grupo.
     */
    override fun obtenerGastosGrupo(idGrupo: Long, idUsuarioActual: Long): List<RespuestaGasto> {
        verificarMiembro(idGrupo, idUsuarioActual)

        val gastosGrupo = gastoRepository.findAllByGrupo_IdOrderByFechaDesc(idGrupo)

        return gastosGrupo.map { it.dto() }
    }

    /**
     * Elimina un gasto, validando que el usuario tenga permiso para ello.
     * Un usuario puede eliminar un gasto si es el pagador o si es administrador del grupo.
     *
     * @param idGrupo El ID del grupo al que pertenece el gasto.
     * @param idUsuarioActual El ID del usuario que realiza la acción.
     * @param idGasto El ID del gasto a eliminar.
     * @throws EntityNotFoundException si el gasto no existe.
     * @throws AccessDeniedException si el usuario no tiene permisos para eliminar el gasto.
     */
    override fun eliminarGasto(idGrupo: Long, idUsuarioActual: Long, idGasto: Long) {
        verificarMiembro(idGrupo, idUsuarioActual)

        val gasto = gastoRepository.findByIdOrNull(idGasto)
            ?: throw EntityNotFoundException("El gasto con ID $idGasto no existe.")

        if (gasto.grupo.id != idGrupo) {
            throw AccessDeniedException("El gasto con ID $idGasto no pertenece al grupo con ID $idGrupo.")
        }

        val membresiaUsuario = membresiaRepository.findByGrupo_IdAndUsuario_Id(idGrupo, idUsuarioActual)!!

        if (gasto.pagador.id != idUsuarioActual && membresiaUsuario.rol != RolGrupo.ADMIN) {
            throw AccessDeniedException("No tienes permiso para eliminar este gasto. Debes ser el pagador o un administrador del grupo.")
        }

        gastoRepository.delete(gasto)
    }

    /**
     * Función de ayuda privada para verificar si un usuario es miembro de un grupo.
     *
     * @param idGrupo El ID del grupo a verificar.
     * @param idUsuario El ID del usuario a verificar.
     * @throws AccessDeniedException si el usuario no es miembro del grupo.
     */
    private fun verificarMiembro(idGrupo: Long, idUsuario: Long) {
        membresiaRepository.findByGrupo_IdAndUsuario_Id(idGrupo, idUsuario)
            ?: throw AccessDeniedException("Acceso denegado. No eres miembro del grupo con ID $idGrupo.")
    }

}