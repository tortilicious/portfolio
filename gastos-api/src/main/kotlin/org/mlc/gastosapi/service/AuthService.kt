package org.mlc.gastosapi.service

import org.mlc.gastosapi.dto.auth.PeticionLogin
import org.mlc.gastosapi.dto.auth.PeticionRegistro
import org.mlc.gastosapi.dto.auth.RespuestaAuth


/**
 * Interfaz para el servicio de autenticación.
 * Define las operaciones de registro e inicio de sesión.
 */
interface AuthService {
    fun registrarUsuario(peticion: PeticionRegistro): RespuestaAuth
    fun login(peticion: PeticionLogin): RespuestaAuth
    fun eliminarUsuario(id: Long)
}