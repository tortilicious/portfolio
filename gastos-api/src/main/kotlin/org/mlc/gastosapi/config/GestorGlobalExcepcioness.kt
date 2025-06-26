package org.mlc.gastosapi.config

import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * DTO simple para estructurar las respuestas de error.
 */
data class RespuestaError(val status: Int, val message: String?)

/**
 * Guardián global que intercepta las excepciones lanzadas por los controladores
 * y las transforma en respuestas HTTP claras y estandarizadas.
 */
@RestControllerAdvice
class GestorGlobalExcepciones {

    /**
     * Maneja las excepciones 'EntityNotFoundException'.
     * Esto ocurre cuando se busca un recurso (ej: un grupo o usuario) por su ID y no se encuentra.
     * Devuelve un código de estado 404 Not Found.
     *
     * @param ex La excepción capturada.
     * @return Un ResponseEntity con el estado 404 y un mensaje de error.
     */
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFound(ex: EntityNotFoundException): ResponseEntity<RespuestaError> {
        val respuestaError = RespuestaError(
            status = HttpStatus.NOT_FOUND.value(),
            message = ex.message
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaError)
    }

    /**
     * Maneja las excepciones 'AccessDeniedException' de Spring Security.
     * Ocurre cuando un usuario autenticado intenta realizar una acción para la que no tiene permisos
     * (ej: un 'MEMBER' intentando añadir a otro miembro).
     * Devuelve un código de estado 403 Forbidden.
     *
     * @param ex La excepción capturada.
     * @return Un ResponseEntity con el estado 403 y un mensaje de error.
     */
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(ex: AccessDeniedException): ResponseEntity<RespuestaError> {
        val respuestaError = RespuestaError(
            status = HttpStatus.FORBIDDEN.value(),
            message = ex.message
        )
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuestaError)
    }

    /**
     * Maneja las excepciones 'IllegalStateException'.
     * Normalmente se usan para violaciones de lógica de negocio
     * (ej: intentar añadir a un usuario que ya es miembro).
     * Devuelve un código de estado 409 Conflict, que es más específico que 400 Bad Request.
     *
     * @param ex La excepción capturada.
     * @return Un ResponseEntity con el estado 409 y un mensaje de error.
     */
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(ex: IllegalStateException): ResponseEntity<RespuestaError> {
        val respuestaError = RespuestaError(
            status = HttpStatus.CONFLICT.value(),
            message = ex.message
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuestaError)
    }

    /**
     * Atrapa cualquier otra excepción no controlada específicamente.
     * Esto previene que se filtren trazas de error al cliente y asegura que siempre
     * se devuelva una respuesta JSON estandarizada.
     * Devuelve un código de estado 500 Internal Server Error.
     *
     * @param ex La excepción genérica capturada.
     * @return Un ResponseEntity con el estado 500 y un mensaje genérico.
     */
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<RespuestaError> {
        val respuestaError = RespuestaError(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = "Ha ocurrido un error inesperado en el servidor."
        )
        // Imprimimos la traza del error en la consola del servidor para poder depurarlo.
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError)
    }
}