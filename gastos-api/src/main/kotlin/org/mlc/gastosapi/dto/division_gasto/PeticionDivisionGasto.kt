package org.mlc.gastosapi.dto.division_gasto

import java.math.BigDecimal


/**
 * DTO que representa una parte de la división de un gasto en una petición.
 *
 * @property deudorId El ID del usuario que debe esta parte.
 * @property montoAdeudado La cantidad exacta que debe pagar.
 */
data class PeticionDivisionGasto(
    val deudorId: Long,
    val montoAdeudado: BigDecimal
)
