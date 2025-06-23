package org.mlc.gastosapi.dto.saldo

import org.mlc.gastosapi.dto.usuario.RespuestaUsuario
import java.math.BigDecimal


/**
 * Representa una única transacción simplificada para saldar una deuda.
 *
 * @property deudor El usuario que debe realizar el pago.
 * @property acreedor El usuario que debe recibir el pago.
 * @property monto La cantidad exacta que se debe transferir.
 */
data class RespuestaTransaccion(
    val deudor: RespuestaUsuario,
    val acreedor: RespuestaUsuario,
    val monto: BigDecimal
)
