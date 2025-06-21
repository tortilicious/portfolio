package org.mlc.gastosapi.repository

import org.mlc.gastosapi.model.Gasto
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repositorio para la entidad Gasto.
 */
interface GastoRepository : JpaRepository<Gasto, Long> {
    /**
     * Busca todos los gastos de un grupo específico, ordenados por fecha descendente (los más nuevos primero).
     * @param grupoId El ID del grupo.
     * @return Una lista de [Gasto]s de ese grupo, ordenados por fecha.
     */
    fun findAllByGrupo_IdOrderByFechaDesc(grupoId: Long): List<Gasto>

    /**
     * Busca todos los gastos pagados por un usuario específico.
     * @param pagadorId El ID del usuario que pagó.
     * @return Una lista de [Gasto]s pagados por ese usuario.
     */
    fun findByPagador_Id(pagadorId: Long): List<Gasto>
}
