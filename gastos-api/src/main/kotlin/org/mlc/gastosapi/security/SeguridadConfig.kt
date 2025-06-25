package org.mlc.gastosapi.security

import org.mlc.gastosapi.service.impl.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * Clase de configuración central para Spring Security.
 * Define la cadena de filtros de seguridad, los proveedores de autenticación
 * y otros componentes necesarios para proteger la API.
 *
 * @property userDetailsService El servicio que sabe cómo cargar un usuario por su email.
 * @property jwtAuthenticationFilter El filtro personalizado para procesar tokens JWT en cada petición.
 */
@Configuration
@EnableWebSecurity
class SeguridadConfig(
    private val userDetailsService: UserDetailsServiceImpl,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    /**
     * Define la cadena de filtros de seguridad principal de la aplicación.
     * Este bean es el corazón de la configuración de seguridad web, donde se define
     * el comportamiento para cada petición HTTP entrante.
     *
     * @param http El objeto constructor para configurar la seguridad web.
     * @return La cadena de filtros de seguridad construida.
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // Desactiva la protección CSRF (Cross-Site Request Forgery).
            // Es una práctica estándar para APIs REST 'stateless'.
            .csrf { it.disable() }

            // Configura las reglas de autorización para las peticiones HTTP.
            .authorizeHttpRequests { auth ->
                auth
                    // Permite el acceso público (sin autenticación) a estas rutas específicas.
                    .requestMatchers(
                        "/api/auth/**",      // Endpoints de autenticación (login/registro).
                        "/api-docs.html/**", // Documentación de la API.
                        "/swagger-ui/**",    // Interfaz de Swagger.
                        "/v3/api-docs/**"    // Definición OpenAPI.
                    ).permitAll()
                    // Exige autenticación para cualquier otra petición.
                    .anyRequest().authenticated()
            }

            // Configura la gestión de sesiones como 'stateless'.
            // No se crearán sesiones en el servidor; cada petición se valida con el token.
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            // Registra nuestro proveedor de autenticación personalizado.
            .authenticationProvider(authenticationProvider())

            // Añade nuestro filtro JWT a la cadena, antes del filtro de autenticación por defecto.
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    /**
     * Define el "Proveedor de Autenticación" (AuthenticationProvider).
     * Le dice a Spring Security CÓMO debe autenticar a los usuarios, especificando
     * qué servicio usar para encontrar usuarios y qué codificador para verificar contraseñas.
     *
     * @return Un AuthenticationProvider completamente configurado.
     */
    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider(userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    /**
     * Define el bean para el codificador de contraseñas que se usará en toda la aplicación.
     * Se utiliza BCrypt, que es el estándar de la industria para hashear contraseñas de forma segura.
     *
     * @return una instancia de BCryptPasswordEncoder.
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * Expone el AuthenticationManager de Spring como un bean.
     * El framework lo necesita internamente para gestionar el proceso de autenticación global.
     *
     * @param config La configuración de autenticación de Spring.
     * @return El AuthenticationManager gestionado por Spring.
     */
    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}