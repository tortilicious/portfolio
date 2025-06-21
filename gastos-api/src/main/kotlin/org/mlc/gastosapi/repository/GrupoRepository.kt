package org.mlc.gastosapi.repository

import org.mlc.gastosapi.model.Grupo
import org.mlc.gastosapi.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repositorio para la entidad Grupo.
 */
interface GrupoRepository : JpaRepository<Grupo, Long> {
}