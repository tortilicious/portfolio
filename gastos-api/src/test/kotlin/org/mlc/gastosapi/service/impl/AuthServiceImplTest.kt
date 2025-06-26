package org.mlc.gastosapi.service.impl

import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mlc.gastosapi.dto.auth.PeticionLogin
import org.mlc.gastosapi.dto.auth.PeticionRegistro
import org.mlc.gastosapi.model.Usuario
import org.mlc.gastosapi.repository.UsuarioRepository
import org.mlc.gastosapi.security.JwtProvider
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder


@ExtendWith(MockitoExtension::class)
class AuthServiceImplTest {

    @Mock
    private lateinit var usuarioRepository: UsuarioRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var jwtProvider: JwtProvider

    @InjectMocks
    private lateinit var authService: AuthServiceImpl

    // --- Tests para registrarUsuario ---

    @Test
    fun `dado un nuevo usuario valido, cuando se registra, deberia devolver una RespuestaAuth`() {
        // ARRANGE
        val peticion = PeticionRegistro("Test User", "test@user.com", "password123")
        `when`(usuarioRepository.findByEmail(peticion.email)).thenReturn(null)
        `when`(passwordEncoder.encode(peticion.password)).thenReturn("hashed_password_string")
        `when`(jwtProvider.generarToken(peticion.email)).thenReturn("fake.jwt.token")
        `when`(usuarioRepository.save(any(Usuario::class.java))).thenAnswer {
            val usuarioPasado = it.getArgument<Usuario>(0)
            usuarioPasado.copy(id = 1L)
        }

        // ACT
        val resultado = authService.registrarUsuario(peticion)

        // ASSERT
        assertThat(resultado).isNotNull
        assertThat(resultado.token).isEqualTo("fake.jwt.token")
        assertThat(resultado.usuario.id).isEqualTo(1L)
        verify(usuarioRepository).save(any(Usuario::class.java))
    }

    @Test
    fun `dado un email que ya existe, cuando se intenta registrar, deberia lanzar una excepcion`() {
        // ARRANGE
        val peticion = PeticionRegistro("Test User", "existente@user.com", "password123")
        val usuarioExistente = Usuario(id = 1L, nombre = "Usuario Existente", email = peticion.email, passwordHash = "hash")
        `when`(usuarioRepository.findByEmail(peticion.email)).thenReturn(usuarioExistente)

        // ACT & ASSERT
        val excepcion = assertThrows<Exception> {
            authService.registrarUsuario(peticion)
        }
        assertThat(excepcion.message).isEqualTo("El usuario ya existe")
    }

    // --- Tests para login ---

    @Test
    fun `dado un usuario con credenciales validas, cuando intenta logear, deberia devolver una RespuestaAuth`() {
        // ARRANGE
        val peticion = PeticionLogin("test@user.com", "password123")
        val usuarioExistente = Usuario(id = 1L, nombre = "Test User", email = peticion.email, passwordHash = "hashed_password")
        `when`(usuarioRepository.findByEmail(peticion.email)).thenReturn(usuarioExistente)
        `when`(passwordEncoder.matches(peticion.password, usuarioExistente.passwordHash)).thenReturn(true)
        `when`(jwtProvider.generarToken(peticion.email)).thenReturn("token.real.generado")

        // ACT
        val resultado = authService.login(peticion)

        // ASSERT
        assertThat(resultado).isNotNull
        assertThat(resultado.token).isEqualTo("token.real.generado")
        assertThat(resultado.usuario.id).isEqualTo(1L)
    }

    /**
     * Test para el caso de error donde el usuario no existe.
     * Verifica que se lanza la excepción correcta.
     */
    @Test
    fun `dado un email que no existe, cuando intenta logear, deberia lanzar EntityNotFoundException`() {
        // --- ARRANGE ---
        val peticion = PeticionLogin("no.existe@user.com", "password123")

        // Le decimos al mock que, cuando busque este email, no encuentre nada (devuelva null).
        `when`(usuarioRepository.findByEmail(peticion.email)).thenReturn(null)

        // --- ACT & ASSERT ---
        // Verificamos que la llamada al servicio lanza la excepción que esperamos.
        val excepcion = assertThrows<EntityNotFoundException> {
            authService.login(peticion)
        }

        // Verificamos el mensaje para asegurar que es el error correcto por seguridad.
        assertThat(excepcion.message).isEqualTo("Credenciales incorrectas.")
    }

    /**
     * Test para el caso de error donde la contraseña es incorrecta.
     * Verifica que se lanza la excepción correcta.
     */
    @Test
    fun `dado un email valido pero una contraseña incorrecta, cuando intenta logear, deberia lanzar EntityNotFoundException`() {
        // --- ARRANGE ---
        val peticion = PeticionLogin("test@user.com", "passwordIncorrecta")
        val usuarioExistente = Usuario(id = 1L, nombre = "Test User", email = peticion.email, passwordHash = "hashed_password")

        // Simulamos que el usuario SÍ existe.
        `when`(usuarioRepository.findByEmail(peticion.email)).thenReturn(usuarioExistente)

        // ¡La preparación clave! Simulamos que la comparación de contraseñas falla (devuelve false).
        `when`(passwordEncoder.matches(peticion.password, usuarioExistente.passwordHash)).thenReturn(false)

        // --- ACT & ASSERT ---
        val excepcion = assertThrows<EntityNotFoundException> {
            authService.login(peticion)
        }
        assertThat(excepcion.message).isEqualTo("Credenciales incorrectas.")
    }
}
