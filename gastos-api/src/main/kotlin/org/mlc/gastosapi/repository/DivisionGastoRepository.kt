package org.mlc.gastosapi.repository

import org.mlc.gastosapi.model.DivisionGasto
import org.springframework.data.jpa.repository.JpaRepository


interface DivisionGastoRepository : JpaRepository<DivisionGasto, Long> {
    /**
     * Busca todas las divisiones de deuda de un usuario específico.
     * @param deudorId El ID del usuario que debe.
     * @return Una lista de las deudas de ese usuario.
     */
    fun findByDeudor_Id(deudorId: Long): List<DivisionGasto>

    /**
     * Busca todas las divisiones de un gasto específico.
     * @param gastoId El ID del gasto a desglosar.
     * @return Una lista con el desglose de la deuda de ese gasto.
     */
    fun findByGasto_Id(gastoId: Long): List<DivisionGasto>
}