package org.mlc.gastosapi.repository

import org.mlc.gastosapi.model.Membresia
import org.springframework.data.jpa.repository.JpaRepository

interface MembresiaRepository : JpaRepository<Membresia, Long> {
    fun findByGrupo_IdAndUsuario_Id(idGrupo: Long, idUsuario: Long): Membresia?
}