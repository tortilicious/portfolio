package org.mlc.tasktracker.exception

import org.mlc.tasktracker.dto.error.ErrorResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

/**
 * Global exception handler for the application's REST controllers.
 *
 * This class uses [@ControllerAdvice] to centralize exception handling across all
 * [@RestController] classes, providing custom HTTP responses for various exceptions.
 */
@ControllerAdvice
class GlobalExceptionHandler {

    /**
     * Handles [UserAlreadyExistException] and returns an HTTP 409 Conflict response.
     * This indicates that a resource (user) could not be created because it already exists.
     *
     * @param ex The [UserAlreadyExistException] that was thrown.
     * @param request The current [WebRequest] to extract request details like the URI.
     * @return A [ResponseEntity] containing an [ErrorResponseDTO] body and HTTP status 409 (Conflict).
     */
    @ExceptionHandler(UserAlreadyExistException::class)
    fun handleUserAlreadyExistException(
        ex: UserAlreadyExistException,
        request: WebRequest
    ): ResponseEntity<ErrorResponseDTO> {
        val path = request.getDescription(false).removePrefix("uri=")
        val errorResponse = ErrorResponseDTO(
            status = HttpStatus.CONFLICT.value(),
            error = HttpStatus.CONFLICT.reasonPhrase,
            message = ex.message,
            path = path
        )
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    /**
     * Handles [UserNotFoundException] and returns an HTTP 404 Not Found response.
     * This indicates that the requested user resource could not be found.
     *
     * @param ex The [UserNotFoundException] that was thrown.
     * @param request The current [WebRequest] to extract request details.
     * @return A [ResponseEntity] containing an [ErrorResponseDTO] body and HTTP status 404 (Not Found).
     */
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        ex: UserNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponseDTO> {
        val path = request.getDescription(false).removePrefix("uri=")
        val errorResponse = ErrorResponseDTO(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = ex.message,
            path = path
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    /**
     * Handles [TaskListNotFoundException] and returns an HTTP 404 Not Found response.
     * This indicates that the requested task list resource could not be found.
     *
     * @param ex The [TaskListNotFoundException] that was thrown.
     * @param request The current [WebRequest] to extract request details.
     * @return A [ResponseEntity] containing an [ErrorResponseDTO] body and HTTP status 404 (Not Found).
     */
    @ExceptionHandler(TaskListNotFoundException::class)
    fun handleTaskListNotFoundException(
        ex: TaskListNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponseDTO> {
        val path = request.getDescription(false).removePrefix("uri=")
        val errorResponse = ErrorResponseDTO(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = ex.message,
            path = path
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    /**
     * Handles [TaskNotFoundException] and returns an HTTP 404 Not Found response.
     * This indicates that the requested task resource could not be found.
     *
     * @param ex The [TaskNotFoundException] that was thrown.
     * @param request The current [WebRequest] to extract request details.
     * @return A [ResponseEntity] containing an [ErrorResponseDTO] body and HTTP status 404 (Not Found).
     */
    @ExceptionHandler(TaskNotFoundException::class)
    fun handleTaskNotFoundException(
        ex: TaskNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponseDTO> {
        val path = request.getDescription(false).removePrefix("uri=")
        val errorResponse = ErrorResponseDTO(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = ex.message,
            path = path
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    /**
     * Handles [AuthorizationException] and returns an HTTP 403 Forbidden response.
     * This indicates that the client does not have permission to access the resource,
     * even if they are authenticated.
     *
     * @param ex The [AuthorizationException] that was thrown.
     * @param request The current [WebRequest] to extract request details.
     * @return A [ResponseEntity] containing an [ErrorResponseDTO] body and HTTP status 403 (Forbidden).
     */
    @ExceptionHandler(AuthorizationException::class)
    fun handleAuthorizationException(
        ex: AuthorizationException,
        request: WebRequest
    ): ResponseEntity<ErrorResponseDTO> {
        val path = request.getDescription(false).removePrefix("uri=")
        val errorResponse = ErrorResponseDTO(
            status = HttpStatus.FORBIDDEN.value(),
            error = HttpStatus.FORBIDDEN.reasonPhrase,
            message = ex.message,
            path = path
        )
        return ResponseEntity(errorResponse, HttpStatus.FORBIDDEN)
    }

    /**
     * Handles [InvalidCredentialsException] and returns an HTTP 401 Unauthorized response.
     * This typically indicates that authentication failed (e.g., wrong username/password).
     *
     * @param ex The [InvalidCredentialsException] that was thrown.
     * @param request The current [WebRequest] to extract request details.
     * @return A [ResponseEntity] containing an [ErrorResponseDTO] body and HTTP status 401 (Unauthorized).
     */
    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentialsException(
        ex: InvalidCredentialsException,
        request: WebRequest
    ): ResponseEntity<ErrorResponseDTO> {
        val path = request.getDescription(false).removePrefix("uri=")
        val errorResponse = ErrorResponseDTO(
            status = HttpStatus.UNAUTHORIZED.value(),
            error = HttpStatus.UNAUTHORIZED.reasonPhrase,
            message = ex.message ?: "Invalid credentials provided.",
            path = path
        )
        return ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED)
    }

    /*
    // You can add more exception handlers here for other custom or Spring-specific exceptions.
    // For example, for validation errors from @Valid on DTOs:
    // import org.springframework.web.bind.MethodArgumentNotValidException
    // @ExceptionHandler(MethodArgumentNotValidException::class)
    // fun handleValidationExceptions(
    //     ex: MethodArgumentNotValidException,
    //     request: WebRequest
    // ): ResponseEntity<ErrorResponseDTO> {
    //     val errors = ex.bindingResult.fieldErrors.joinToString { "${it.field}: ${it.defaultMessage}" }
    //     val path = request.getDescription(false).removePrefix("uri=")
    //     val errorResponse = ErrorResponseDTO(
    //         status = HttpStatus.BAD_REQUEST.value(),
    //         error = HttpStatus.BAD_REQUEST.reasonPhrase,
    //         message = "Validation failed: $errors",
    //         path = path
    //     )
    //     return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    // }

    // A generic handler for any other unexpected exceptions not caught specifically.
    // It's generally good practice to log the full stack trace of such exceptions.
    // @ExceptionHandler(Exception::class)
    // fun handleGenericException(
    //     ex: Exception,
    //     request: WebRequest
    // ): ResponseEntity<ErrorResponseDTO> {
    //     val path = request.getDescription(false).removePrefix("uri=")
    //     val errorResponse = ErrorResponseDTO(
    //         status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
    //         error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
    //         message = "An unexpected error occurred: ${ex.localizedMessage}",
    //         path = path
    //     )
    //     return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    // }
    */
}