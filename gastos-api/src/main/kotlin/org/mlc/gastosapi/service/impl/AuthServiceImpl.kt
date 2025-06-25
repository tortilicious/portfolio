package org.mlc.gastosapi.service.impl

import jakarta.persistence.EntityNotFoundException
import org.mlc.gastosapi.dto.auth.PeticionLogin
import org.mlc.gastosapi.dto.auth.PeticionRegistro
import org.mlc.gastosapi.dto.auth.RespuestaAuth
import org.mlc.gastosapi.model.Usuario
import org.mlc.gastosapi.repository.UsuarioRepository
import org.mlc.gastosapi.security.JwtProvider
import org.mlc.gastosapi.service.AuthService
import org.mlc.gastosapi.utils.dto // Asumo que esta es la importación para tu función de mapeo .Dto()
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Implementación del servicio de autenticación.
 * Gestiona la lógica de negocio para el registro, login y eliminación de usuarios.
 *
 * @property usuarioRepository Repositorio para interactuar con los datos de los usuarios.
 * @property passwordEncoder Codificador para hashear y verificar contraseñas de forma segura.
 * @property jwtProvider El proveedor utilizado para generar los tokens JWT.
 */
@Service
@Transactional
class AuthServiceImpl(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider
) : AuthService {

    /**
     * Registra un nuevo usuario en el sistema.
     * Primero comprueba si el email ya existe para evitar duplicados.
     *
     * @param peticion DTO con los datos del usuario a registrar.
     * @return Una [RespuestaAuth] con un token JWT real y los datos del nuevo usuario.
     * @throws IllegalStateException si el email proporcionado ya está en uso.
     */
    override fun registrarUsuario(peticion: PeticionRegistro): RespuestaAuth {
        // 1.- comprobar si existe el usuario
        val usuario = usuarioRepository.findByEmail(peticion.email)
        usuario?.let { throw Exception("El usuario ya existe") }

        // 2.- Crear y guardar el nuevo usuario
        val nuevoUsuario = Usuario(
            nombre = peticion.nombre,
            passwordHash = passwordEncoder.encode(peticion.password),
            email = peticion.email
        )

        usuarioRepository.save(nuevoUsuario)

        // 3.- Generar un token JWT mediante JwtProvider
        val token = jwtProvider.generarToken(peticion.email)

        // 4.- Devolver la respuesta
        val respuesta = RespuestaAuth(token, nuevoUsuario.dto())
        return respuesta
    }

    /**
     * Autentica a un usuario y le proporciona un token de acceso JWT real.
     *
     * @param peticion DTO con las credenciales de inicio de sesión.
     * @return Una [RespuestaAuth] con el token JWT real y los datos del usuario.
     * @throws EntityNotFoundException si las credenciales son incorrectas.
     */
    override fun login(peticion: PeticionLogin): RespuestaAuth {
        // 1. Buscar usuario por email. Si no existe, lanza una excepción.
        val usuario = usuarioRepository.findByEmail(peticion.email)
            ?: throw EntityNotFoundException("Credenciales incorrectas.")

        // 2. Verificar la contraseña. Si no coincide, lanza una excepción.
        if (!passwordEncoder.matches(peticion.password, usuario.passwordHash)) {
            throw EntityNotFoundException("Credenciales incorrectas.")
        }

        // 3. Generar un token JWT (simulado por ahora)
        val token = jwtProvider.generarToken(peticion.email)

        // 4. Devolver la RespuestaAuth
        return RespuestaAuth(token, usuario.dto())
    }

    /**
     * Elimina un usuario del sistema por su ID.
     *
     * @param id El ID del usuario a eliminar.
     * @throws EntityNotFoundException si no se encuentra ningún usuario con ese ID.
     */
    override fun eliminarUsuario(id: Long) {
        // Se comprueba si el usuario existe antes de borrar para lanzar un error claro.
        val usuario = usuarioRepository.findByIdOrNull(id)
            ?: throw EntityNotFoundException("Usuario no existe.")
        usuarioRepository.deleteById(id)
    }
}