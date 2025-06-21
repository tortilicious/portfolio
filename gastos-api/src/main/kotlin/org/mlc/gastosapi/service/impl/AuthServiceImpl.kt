package org.mlc.gastosapi.service.impl

import org.mlc.gastosapi.dto.auth.PeticionLogin
import org.mlc.gastosapi.dto.auth.PeticionRegistro
import org.mlc.gastosapi.dto.auth.RespuestaAuth
import org.mlc.gastosapi.repository.GrupoRepository
import org.mlc.gastosapi.repository.UsuarioRepository
import org.mlc.gastosapi.service.AuthService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthServiceImpl(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val grupoRepository: GrupoRepository
) : AuthService {
    override fun registrarUsuario(peticion: PeticionRegistro): RespuestaAuth {
        // 1.- comprobar si existe el usuario
        val usuario = usuarioRepository.findByEmail(peticion.email)
        usuario?.let { throw Exception("El usuario ya existe") }

        // 2.-

    }

    override fun login(peticion: PeticionLogin): RespuestaAuth {
        TODO("Not yet implemented")
    }
}