package org.mlc.gastosapi.dto.saldo

import org.mlc.gastosapi.dto.usuario.RespuestaUsuario
import java.math.BigDecimal


/**
 * Representa el balance neto final de un único usuario.
 *
 * @property usuario Los datos del usuario.
 * @property balance El monto final. Positivo si se le debe dinero, negativo si él debe dinero.
 */
data class RespuestaBalanceUsuario(
    val usuario: RespuestaUsuario,
    val balance: BigDecimal
)
