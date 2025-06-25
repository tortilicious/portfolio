package org.mlc.gastosapi.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.SIG.HS512
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

/**
 * Componente responsable de la generación, validación y extracción de información
 * de los tokens JWT (JSON Web Tokens).
 * Actúa como la autoridad central para todo lo relacionado con JWT en la aplicación.
 */
@Component
class JwtProvider {

    /**
     * La clave secreta utilizada para firmar y verificar los tokens.
     * Se inyecta desde el fichero `application.properties`.
     * Es crucial que esta clave sea larga, compleja y se mantenga segura.
     */
    @Value("\${jwt.secret}")
    private lateinit var secreto: String

    /**
     * El tiempo de validez de un token en milisegundos.
     * Se inyecta desde el fichero `application.properties`.
     */
    @Value("\${jwt.expiration-in-ms}")
    private var expiraEnMs: Long = 0L

    /**
     * La clave de firma HMAC-SHA, generada a partir del secreto.
     * Se inicializa de forma perezosa (`lazy`) para asegurar que se cree una sola vez
     * cuando se necesite por primera vez.
     */
    private val clave by lazy { Keys.hmacShaKeyFor(secreto.toByteArray()) }

    /**
     * Genera un nuevo token JWT para un usuario específico.
     *
     * @param email El email del usuario, que se almacenará como el "subject" (sujeto) del token.
     * @return El token JWT como una cadena de texto compacta (String).
     */
    fun generarToken(email: String): String {
        val ahora = Date()
        val fechaExpiracion = Date(ahora.time + expiraEnMs)

        return Jwts.builder()
            .subject(email)
            .issuedAt(ahora)
            .expiration(fechaExpiracion)
            .signWith(clave, HS512)
            .compact()
    }

    /**
     * Extrae el email del usuario (el "subject") a partir de un token JWT.
     * Este método asume que el token ya ha sido validado.
     *
     * @param token El token JWT del cual se extraerá el email.
     * @return El email del usuario contenido en el token.
     * @throws io.jsonwebtoken.JwtException si el token es inválido o ha expirado.
     */
    fun obtenerEmailToken(token: String): String {
        return Jwts.parser()
            .verifyWith(clave) // Prepara la verificación con nuestra clave.
            .build()           // Construye el objeto "parser".
            .parseSignedClaims(token) // Parsea y valida el token.
            .payload
            .subject       // Extrae el "subject". En nuestro caso el 'email' del usuario
    }

    /**
     * Valida si un token JWT es auténtico y no ha expirado.
     *
     * @param token El token a validar.
     * @return `true` si el token tiene una firma válida y no está caducado, `false` en caso contrario.
     */
    fun validarToken(token: String): Boolean {
        try {
            // Intenta parsear el token. Si tiene éxito, la firma es correcta y no ha expirado.
            Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
            return true
        } catch (e: SignatureException) {
            // Captura específicamente errores de firma inválida.
            println("Token invalido: ${e.message}")
            return false
        }
    }
}