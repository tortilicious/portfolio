package org.mlc.gastosapi.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.mlc.gastosapi.service.impl.UserDetailsServiceImpl
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Filtro de autenticación personalizado que se ejecuta una vez por cada petición HTTP.
 * Su responsabilidad es interceptar las peticiones, validar el token JWT que viene
 * en el encabezado de autorización y establecer la autenticación del usuario en el
 * contexto de seguridad de Spring.
 *
 * @property jwtProvider El proveedor utilizado para validar y extraer información de los tokens JWT.
 * @property userDetailsService El servicio para cargar los detalles del usuario desde la base de datos.
 */
@Component
class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider,
    private val userDetailsService: UserDetailsServiceImpl
) : OncePerRequestFilter() {

    /**
     * El metodo principal del filtro que procesa cada petición entrante.
     *
     * @param request La petición HTTP entrante.
     * @param response La respuesta HTTP que se enviará.
     * @param filterChain La cadena de filtros de seguridad, para pasar la petición al siguiente filtro.
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1. Intenta extraer el token JWT del encabezado de la petición.
        val token = obtenerTokenPeticion(request)

        // 2. Si se encontró un token y es válido...
        if (token != null && jwtProvider.validarToken(token)) {
            // 3. Extrae el email del usuario (el "subject") del token.
            val email = jwtProvider.obtenerEmailToken(token)

            // 4. Carga los detalles del usuario desde la base de datos usando el email.
            val userDetails = userDetailsService.loadUserByUsername(email)

            // 5. Crea un objeto de autenticación para Spring Security.
            val authToken = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            )
            authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

            // 6. Establece la autenticación en el contexto de seguridad global.
            // A partir de este momento, Spring sabe que la petición actual está autenticada.
            SecurityContextHolder.getContext().authentication = authToken
        }

        // 7. Pasa la petición al siguiente filtro en la cadena de seguridad.
        filterChain.doFilter(request, response)
    }

    /**
     * Función de ayuda para extraer el token JWT del encabezado "Authorization".
     * El estándar es que el token venga en el formato "Bearer <token>".
     *
     * @param request La petición HTTP de la cual se extraerá el encabezado.
     * @return El token JWT como una cadena de texto, o `null` si no se encuentra.
     */
    private fun obtenerTokenPeticion(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Devuelve la subcadena que va después de "Bearer " (los primeros 7 caracteres).
            return authHeader.substring(7)
        }
        return null
    }
}