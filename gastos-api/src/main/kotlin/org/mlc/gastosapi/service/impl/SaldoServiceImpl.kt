package org.mlc.gastosapi.service.impl

import org.mlc.gastosapi.dto.saldo.RespuestaBalanceUsuario
import org.mlc.gastosapi.dto.saldo.RespuestaSaldoGrupo
import org.mlc.gastosapi.dto.saldo.RespuestaTransaccion
import org.mlc.gastosapi.repository.GastoRepository
import org.mlc.gastosapi.repository.MembresiaRepository
import org.mlc.gastosapi.repository.UsuarioRepository
import org.mlc.gastosapi.service.SaldoService
import org.mlc.gastosapi.utils.dto // Asumo que la importación para tu función de mapeo es esta
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/**
 * Implementación del servicio para el cálculo de saldos y deudas de un grupo.
 *
 * @property gastoRepository Repositorio para acceder a los datos de los Gastos.
 * @property membresiaRepository Repositorio para gestionar las relaciones entre usuarios y grupos.
 * @property usuarioRepository Repositorio para acceder a los datos de los Usuarios.
 */
@Service
@Transactional(readOnly = true)
class SaldoServiceImpl(
    private val gastoRepository: GastoRepository,
    private val membresiaRepository: MembresiaRepository,
    private val usuarioRepository: UsuarioRepository,
) : SaldoService {

    /**
     * Calcula y devuelve el estado de los saldos para un grupo específico.
     * El proceso implica:
     * 1. Calcular el balance neto de cada miembro (total pagado vs. total adeudado).
     * 2. Aplicar un algoritmo para encontrar el número mínimo de transacciones para saldar las cuentas.
     * 3. Ensamblar una respuesta DTO con toda la información.
     *
     * @param idGrupo El ID del grupo para el que se calculará el saldo.
     * @param idUsuarioActual El ID del usuario que solicita la información (para validación de permisos).
     * @return Un DTO [RespuestaSaldoGrupo] con el detalle de los balances y transacciones sugeridas.
     */
    override fun obtenerSaldosDeGrupo(idGrupo: Long, idUsuarioActual: Long): RespuestaSaldoGrupo {
        // --- PASO 1: VERIFICACIÓN ---
        // Se comprueba que el usuario que realiza la petición es miembro del grupo.
        verificarMiembro(idGrupo, idUsuarioActual)

        // --- PASO 2: OBTENCIÓN DE DATOS ---
        // Se obtienen todos los gastos del grupo para realizar los cálculos.
        val gastosGrupo = gastoRepository.findAllByGrupo_IdOrderByFechaDesc(idGrupo)

        // --- PASO 3: CÁLCULO DE BALANCES NETOS ---
        // Se utiliza un mapa para almacenar el balance de cada usuario.
        val balanceUsuarios = mutableMapOf<Long, BigDecimal>()

        // Se itera sobre cada gasto para actualizar los balances.
        gastosGrupo.forEach { gasto ->
            // Al pagador se le suma el monto total del gasto.
            val idPagador = gasto.pagador.id
            balanceUsuarios[idPagador] = balanceUsuarios.getOrDefault(idPagador, BigDecimal.ZERO) + gasto.monto

            // A cada deudor se le resta la cantidad que le corresponde.
            gasto.divisiones.forEach { division ->
                val idDeudor = division.deudor.id
                balanceUsuarios[idDeudor] =
                    balanceUsuarios.getOrDefault(idDeudor, BigDecimal.ZERO) - division.cantidadDebida
            }
        }

        // --- PASO 4: CLASIFICACIÓN DE USUARIOS ---
        // Se dividen los usuarios en dos grupos: los que deben dinero (deudores)
        // y a los que se les debe dinero (acreedores).
        val acreedores = mutableListOf<BalanceUsuario>()
        val deudores = mutableListOf<BalanceUsuario>()

        balanceUsuarios.forEach { (idUsuario, saldo) ->
            if (saldo.compareTo(BigDecimal.ZERO) > 0) {
                acreedores.add(BalanceUsuario(idUsuario, saldo))
            } else if (saldo.compareTo(BigDecimal.ZERO) < 0) {
                deudores.add(BalanceUsuario(idUsuario, saldo))
            }
        }

        // --- PASO 5: ALGORITMO DE SIMPLIFICACIÓN DE DEUDAS ---
        // Se calcula el número mínimo de transacciones para saldar todas las cuentas.
        val pagosFinales = mutableListOf<PagosSugeridos>()

        // Se ordenan las listas para que el algoritmo sea eficiente.
        acreedores.sortByDescending { it.saldo }
        deudores.sortBy { it.saldo }

        // El bucle se ejecuta mientras haya tanto deudores como acreedores.
        while (acreedores.isNotEmpty() && deudores.isNotEmpty()) {
            val acreedor = acreedores.first()
            val deudor = deudores.first()

            // La cantidad a pagar es el mínimo entre lo que uno debe y al otro le deben.
            val cantidadAPagar = minOf(acreedor.saldo, deudor.saldo.abs())

            // Se registra la transacción sugerida.
            pagosFinales.add(PagosSugeridos(
                idAcreedor = acreedor.idUsuario,
                idDeudor = deudor.idUsuario,
                cantidadDebida = cantidadAPagar
            ))

            // Se actualizan los saldos temporales.
            acreedor.saldo -= cantidadAPagar
            deudor.saldo += cantidadAPagar

            // Si un usuario llega a saldo cero, se retira del proceso.
            if (acreedor.saldo.compareTo(BigDecimal.ZERO) == 0) acreedores.remove(acreedor)
            if (deudor.saldo.compareTo(BigDecimal.ZERO) == 0) deudores.remove(deudor)
        }

        // --- PASO 6: CONSTRUCCIÓN DE LA RESPUESTA FINAL ---
        // Se obtienen los datos completos de los usuarios implicados en una sola consulta.
        val idsUsuarios = balanceUsuarios.keys
        val usuariosImplicados = usuarioRepository.findAllById(idsUsuarios)
            .associateBy({ it.id }, { it.dto() })

        // Se mapean los balances calculados al DTO de respuesta.
        val balancesDto = balanceUsuarios.map { (idUsuario, saldo) ->
            RespuestaBalanceUsuario(
                usuario = usuariosImplicados[idUsuario]!!,
                balance = saldo
            )
        }

        // Se mapean las transacciones calculadas al DTO de respuesta.
        val transaccionesDto = pagosFinales.map { pago ->
            RespuestaTransaccion(
                deudor = usuariosImplicados[pago.idDeudor]!!,
                acreedor = usuariosImplicados[pago.idAcreedor]!!,
                monto = pago.cantidadDebida
            )
        }

        // Se ensambla y devuelve el objeto de respuesta final.
        return RespuestaSaldoGrupo(
            idGrupo = idGrupo,
            balances = balancesDto,
            transacciones = transaccionesDto
        )
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

/**
 * Clase de datos privada para los cálculos internos de balances.
 */
private data class BalanceUsuario(
    val idUsuario: Long, var saldo: BigDecimal
)

/**
 * Clase de datos privada para los cálculos internos de transacciones.
 */
private data class PagosSugeridos(
    val idAcreedor: Long, val idDeudor: Long, val cantidadDebida: BigDecimal
)