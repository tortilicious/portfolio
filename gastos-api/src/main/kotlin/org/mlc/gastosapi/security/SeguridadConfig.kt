package org.mlc.gastosapi.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SeguridadConfig {

    /**
     * Define un Bean de PasswordEncoder para que Spring pueda inyectarlo en otras clases.
     * Usamos BCrypt, que es el estándar recomendado para hashear contraseñas.
     */
    @Bean
    fun codificadorPassword(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // 1. DESACTIVAR CSRF: Esta línea soluciona tu problema actual del error 403.
            // Es necesario para APIs REST stateless.
            .csrf { it.disable() }

            // 2. DEFINIR REGLAS DE AUTORIZACIÓN: Aquí le decimos a Spring qué es público y qué es privado.
            .authorizeHttpRequests { auth -> auth
                // 2a. Permite el acceso PÚBLICO a todos los endpoints bajo "/api/auth/".
                .requestMatchers("/api/auth/**").permitAll()
                // 2b. Para cualquier OTRA petición, exige que el usuario esté autenticado.
                .anyRequest().authenticated()
            }

            // 3. GESTIÓN DE SESIONES STATELESS: crucial para APIs con JWT.
            // Le decimos a Spring que NO cree sesiones en el servidor para cada usuario.
            .sessionManagement { session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        return http.build()
    }
}