package org.mlc.gastosapi.controller

import org.mlc.gastosapi.dto.auth.PeticionLogin
import org.mlc.gastosapi.dto.auth.PeticionRegistro
import org.mlc.gastosapi.dto.auth.RespuestaAuth
import org.mlc.gastosapi.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controlador para gestionar los endpoints públicos de autenticación.
 * Se encarga del registro de nuevos usuarios y del inicio de sesión.
 *
 * @property authService El servicio que contiene la lógica de negocio para la autenticación.
 */
@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    /**
     * Endpoint para registrar un nuevo usuario en el sistema.
     * Recibe los datos del nuevo usuario y, si son válidos, lo crea y devuelve
     * un token JWT para su primera sesión.
     *
     * @param peticion DTO con los datos del usuario a registrar (nombre, email, contraseña).
     * @return Una respuesta 201 Created con un token de autenticación y los datos del nuevo usuario.
     */
    @PostMapping("/registro")
    fun registrarNuevoUsuario(@RequestBody peticion: PeticionRegistro): ResponseEntity<RespuestaAuth> {
        val registro = authService.registrarUsuario(peticion)
        return ResponseEntity.status(HttpStatus.CREATED).body(registro)
    }

    /**
     * Endpoint para autenticar a un usuario existente.
     * Recibe las credenciales (email y contraseña) y, si son correctas, devuelve
     * un nuevo token JWT para iniciar una sesión.
     *
     * @param peticion DTO con las credenciales de inicio de sesión.
     * @return Una respuesta 200 OK con un token de autenticación y los datos del usuario.
     */
    @PostMapping("/login")
    fun login(@RequestBody peticion: PeticionLogin): ResponseEntity<RespuestaAuth> {
        val login = authService.login(peticion)
        return ResponseEntity.ok(login) // .ok() es un atajo para .status(HttpStatus.OK)
    }
}