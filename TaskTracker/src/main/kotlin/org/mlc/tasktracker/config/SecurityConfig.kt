package org.mlc.tasktracker.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { it
                //  acceso publico a los endpoints de registro y login
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("api/users/**").permitAll()
                .requestMatchers("api/lists/**").permitAll()

                //  pide auth para el resto de endpoints
                .anyRequest().authenticated()
                }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .formLogin { formLogin -> formLogin.disable() } // Deshabilita el formulario de login
            .httpBasic { httpBasic -> httpBasic.disable() } // Deshabilita la autenticación básica HTTP
            .build()
    }
}