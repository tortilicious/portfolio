package org.mlc.gastosapi.repository

import org.mlc.gastosapi.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repositorio para la entidad Usuario.
 */
interface UsuarioRepository : JpaRepository<Usuario, Long> {
    /**
     * Busca un usuario por su dirección de email.
     * @param email El email del usuario a buscar.
     * @return El [Usuario] encontrado o `null` si no existe.
     */
    fun findByEmail(email: String): Usuario?
}