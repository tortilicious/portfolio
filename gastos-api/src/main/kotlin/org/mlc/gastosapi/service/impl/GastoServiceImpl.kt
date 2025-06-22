package org.mlc.gastosapi.service.impl

import jakarta.persistence.EntityNotFoundException
import org.mlc.gastosapi.dto.gasto.PeticionGasto
import org.mlc.gastosapi.dto.gasto.RespuestaGasto
import org.mlc.gastosapi.model.DivisionGasto
import org.mlc.gastosapi.model.Gasto
import org.mlc.gastosapi.repository.GastoRepository
import org.mlc.gastosapi.repository.GrupoRepository
import org.mlc.gastosapi.repository.MembresiaRepository
import org.mlc.gastosapi.repository.UsuarioRepository
import org.mlc.gastosapi.service.GastoService
import org.mlc.gastosapi.utils.dto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class GastoServiceImpl(
    private val gastoRepository: GastoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val grupoRepository: GrupoRepository,
    private val membresiaRepository: MembresiaRepository
) : GastoService {


    override fun crearGasto(idGrupo: Long, idUsuarioActual: Long, peticion: PeticionGasto): RespuestaGasto {
        // 1. Validaciones previas
        val grupo = grupoRepository.findByIdOrNull(idGrupo)
            ?: throw EntityNotFoundException("El grupo con ID $idGrupo no existe.")

        verificarMiembro(idGrupo, idUsuarioActual)

        // 2. Obtener IDs de todos los miembros del grupo
        val idsMiembrosGrupo = grupo.miembros.map { it.usuario.id }

        // 3. Comprobar que el pagador es miembro del grupo
        if (peticion.pagadorId !in idsMiembrosGrupo) {
            throw Exception("El pagador con ID ${peticion.pagadorId} no es miembro del grupo.")
        }

        // 4. Comprobar que los deudores son miembros del grupo
        peticion.divisionGasto.forEach { division ->
            if (division.deudorId !in idsMiembrosGrupo) {
                throw Exception("El deudor con ID ${division.deudorId} no es miembro del grupo.")
            }
        }

        // 5. Validar que la suma de divisiones coincide con el monto total
        val sumaDivisones = peticion.divisionGasto.sumOf { it.montoAdeudado }
        if (peticion.monto != sumaDivisones) {
            throw IllegalArgumentException("La suma de las divisiones $sumaDivisones no coincide con el emonto total del gasto ${peticion.monto}")
        }

        // 6. Obtenemos el pagador del monto. Podemos asegurar su no nullabilidad por las comprobaciones anteriores
        val pagador = usuarioRepository.findByIdOrNull(peticion.pagadorId)!!

        // 7. Crear el gasto
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

        // 8. Guardar en la base de datos y devolver la respuesta
        nuevoGasto.divisiones.addAll(divisiones)

        val gastoGuardado = gastoRepository.save(nuevoGasto)
        return gastoGuardado.dto()
    }

    override fun obtenerGasto(idGrupo: Long, idUsuarioActual: Long, idGasto: Long): RespuestaGasto {
        TODO("Not yet implemented")
    }

    override fun obtenerGastosGrupo(idGrupo: Long, idUsuarioActual: Long): List<RespuestaGasto> {
        TODO("Not yet implemented")
    }

    override fun eliminarGasto(idGrupo: Long, idUsuarioActual: Long, idGasto: Long) {
        TODO("Not yet implemented")
    }

    /**
     * Función de ayuda privada para verificar si un usuario es miembro de un grupo.
     */
    private fun verificarMiembro(idGrupo: Long, idUsuario: Long) {
        membresiaRepository.findByGrupo_IdAndUsuario_Id(idGrupo, idUsuario)
            ?: throw AccessDeniedException("Acceso denegado. No eres miembro del grupo con ID $idGrupo.")
    }
}