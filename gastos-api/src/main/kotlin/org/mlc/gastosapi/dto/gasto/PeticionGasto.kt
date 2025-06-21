package org.mlc.gastosapi.dto.gasto

import org.mlc.gastosapi.dto.division_gasto.PeticionDivisionGasto
import java.math.BigDecimal


/**
 * DTO para la petición de creación de un nuevo gasto.
 *
 * @property descripcion La descripción del gasto.
 * @property monto El monto total del gasto.
 * @property pagadorId El ID del usuario que realizó el pago.
 * @property divisionGasto Una lista que detalla cómo se divide el gasto entre los miembros.
 */
data class PeticionGasto(
    val descripcion: String,
    val monto: BigDecimal,
    val pagadorId: Long,
    val divisionGasto: List<PeticionDivisionGasto>
)
