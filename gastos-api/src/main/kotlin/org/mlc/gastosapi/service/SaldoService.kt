package org.mlc.gastosapi.service

import org.mlc.gastosapi.dto.saldo.RespuestaSaldoGrupo

interface SaldoService {
    /**
     * Calcula y devuelve el estado de los saldos para un grupo específico.
     * Determina quién debe dinero y a quién se le debe.
     *
     * @param idGrupo El ID del grupo para el que se calculará el saldo.
     * @param idUsuarioActual El ID del usuario que solicita la información (para permisos).
     * @return Un objeto DTO que contiene la lista de saldos y, opcionalmente, las transacciones simplificadas para saldar las cuentas.
     */
    fun obtenerSaldosDeGrupo(idGrupo: Long, idUsuarioActual: Long): RespuestaSaldoGrupo
}