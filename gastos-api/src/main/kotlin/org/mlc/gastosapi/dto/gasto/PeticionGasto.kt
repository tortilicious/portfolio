package org.mlc.gastosapi.dto.gasto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive
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
    @field:NotBlank(message = "La descripción del gasto no puede estar vacía.")
    val descripcion: String,
    @field:Positive(message = "El monto debe ser una cantidad positiva.")
    val monto: BigDecimal,
    val pagadorId: Long,
    @field:NotEmpty(message = "La lista de divisiones no puede estar vacía.")
    val divisionGasto: List<PeticionDivisionGasto>
)
