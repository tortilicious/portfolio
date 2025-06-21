package org.mlc.gastosapi.dto.gasto

import org.mlc.gastosapi.dto.division_gasto.RespuestaDivisionGasto
import org.mlc.gastosapi.dto.usuario.RespuestaUsuario
import java.math.BigDecimal
import java.time.LocalDateTime


/**
 * DTO para representar un gasto en las respuestas de la API.
 *
 * @property id El identificador único del gasto.
 * @property descripcion La descripción del gasto.
 * @property monto El monto total del gasto.
 * @property fecha La fecha y hora en que se registró el gasto.
 * @property pagador Un objeto con el resumen del usuario que pagó.
 * @property divisionGasto Una lista con el detalle de cómo se dividió el gasto.
 */
data class RespuestaGasto(
    val id: Long,
    val descripcion: String,
    val monto: BigDecimal,
    val fecha: LocalDateTime,
    val pagador: RespuestaUsuario,
    val divisionGasto: List<RespuestaDivisionGasto>
)