package org.mlc.gastosapi.service.impl

import org.mlc.gastosapi.repository.UsuarioRepository
import org.mlc.gastosapi.security.CustomUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Implementación de la interfaz UserDetailsService de Spring Security.
 * Actúa como un puente entre el modelo de datos de 'Usuario' de la aplicación
 * y el modelo de 'UserDetails' que Spring Security necesita para realizar la autenticación
 * y autorización.
 *
 * @property usuarioRepository Repositorio para acceder a los datos de los usuarios en la base de datos.
 */
@Service
class UserDetailsServiceImpl(
    private val usuarioRepository: UsuarioRepository
) : UserDetailsService {

    /**
     * Carga los datos de un usuario por su email (que Spring llama 'username').
     * Spring Security llamará a este método automáticamente cuando nuestro filtro
     * necesite verificar la identidad de un usuario a partir del token.
     *
     * @param username El email del usuario a buscar.
     * @return Un objeto [UserDetails] con la información del usuario para Spring Security.
     * @throws UsernameNotFoundException si no se encuentra ningún usuario con ese email.
     */
    override fun loadUserByUsername(username: String): UserDetails {
        // Buscamos nuestro 'Usuario' en la base de datos usando el email.
        val usuario = usuarioRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("Usuario no encontrado con el email: $username")

        // Convertimos nuestro objeto 'Usuario' a un objeto 'UserDetails' que Spring entiende.
        return CustomUserDetails(usuario)
    }
}