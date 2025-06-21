package org.mlc.gastosapi.dto.division_gasto

import org.mlc.gastosapi.dto.usuario.RespuestaUsuario
import java.math.BigDecimal


/**
 * DTO que representa una parte de la división de un gasto en una respuesta.
 *
 * @property deudor Un objeto con el resumen del usuario que debe esta parte.
 * @property montoAdeudado La cantidad que debe pagar.
 */
data class RespuestaDivisionGasto(
    val deudor: RespuestaUsuario,
    val montoAdeudado: BigDecimal
)
