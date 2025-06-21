package org.mlc.gastosapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SeguridadConfig {

    /**
     * Define un Bean de PasswordEncoder para que Spring pueda inyectarlo en otras clases.
     * Usamos BCrypt, que es el estándar recomendado para hashear contraseñas.
     */
    @Bean
    fun codificadorPassword(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}