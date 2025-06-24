package org.mlc.gastosapi.controller

import org.mlc.gastosapi.dto.auth.PeticionLogin
import org.mlc.gastosapi.dto.auth.PeticionRegistro
import org.mlc.gastosapi.dto.auth.RespuestaAuth
import org.mlc.gastosapi.dto.usuario.RespuestaUsuario
import org.mlc.gastosapi.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/registro")
    fun registrarNuevoUsuario(@RequestBody peticion: PeticionRegistro): ResponseEntity<RespuestaAuth> {
        val registro = authService.registrarUsuario(peticion)
        return ResponseEntity.status(HttpStatus.CREATED).body(registro)
    }

    @PostMapping("/login")
    fun login(@RequestBody peticion: PeticionLogin): ResponseEntity<RespuestaAuth> {
        val login = authService.login(peticion)
        return ResponseEntity.status(HttpStatus.OK).body(login)
    }
}