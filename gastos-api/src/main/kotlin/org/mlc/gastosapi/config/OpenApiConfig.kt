package org.mlc.gastosapi.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

/**
 * Configuración central para Springdoc OpenAPI (Swagger).
 * Define la información general de la API y cómo se maneja la seguridad para
 * que se pueda interactuar con los endpoints protegidos desde la UI de Swagger.
 */
@Configuration
@OpenAPIDefinition(
    // 1. Proporciona información general sobre tu API.
    // Esto aparecerá en la parte superior de la página de Swagger.
    info = Info(
        title = "Gastos API",
        version = "1.0.0",
        description = "Una API REST para la gestión de gastos compartidos en grupos."
    ),
    // 2. Aplica el requisito de seguridad ("BearerAuth") a TODOS los endpoints de la API.
    security = [SecurityRequirement(name = "BearerAuth")]
)
@SecurityScheme(
    // 3. Define el esquema de seguridad que estamos usando.
    // Le estamos enseñando a Swagger qué es "BearerAuth".
    name = "BearerAuth", // Un nombre que le damos a nuestro esquema de seguridad.
    description = "Token JWT de autenticación. Introduce 'Bearer' seguido de un espacio y luego el token.",
    scheme = "bearer",   // El tipo de esquema estándar para JWT.
    type = SecuritySchemeType.HTTP, // El tipo de seguridad es a través de HTTP.
    bearerFormat = "JWT", // El formato del token.
    `in` = SecuritySchemeIn.HEADER // El token se envía en un encabezado (Header).
)
class OpenApiConfig {

}
