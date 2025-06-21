package org.mlc.gastosapi.dto.auth

import org.mlc.gastosapi.dto.usuario.RespuestaUsuario


/**
 * DTO para la respuesta de autenticación.
 * Contiene la información del usuario y el token JWT para peticiones futuras.
 * @property token El token JWT generado.
 * @property usuario Los detalles del usuario autenticado.
 */
data class RespuestaAuth(
    val token: String,
    val usuario: RespuestaUsuario
)
