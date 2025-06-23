package org.mlc.gastosapi.dto.saldo


/**
 * DTO principal que contiene el resultado completo del cálculo de saldos para un grupo.
 *
 * @property idGrupo El ID del grupo al que pertenecen estos saldos.
 * @property balances La lista con el balance neto de cada miembro del grupo.
 * @property transacciones La lista de pagos recomendados para saldar todas las deudas.
 */
data class RespuestaSaldoGrupo(
    val idGrupo: Long,
    val balances: List<RespuestaBalanceUsuario>,
    val transacciones: List<RespuestaTransaccion>
)
