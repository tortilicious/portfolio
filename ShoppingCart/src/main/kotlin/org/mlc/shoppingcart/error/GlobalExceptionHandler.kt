package org.mlc.shoppingcart.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Global exception handler for the shopping cart API.
 *
 * This class provides centralized exception handling for various custom and standard exceptions
 * occurring across controllers, ensuring consistent error responses.
 */
@ControllerAdvice
class GlobalExceptionHandler {

    /**
     * Handles exceptions related to resources not found (HTTP 404 Not Found).
     * This includes [ProductNotFoundException], [ImageNotFoundException], and [CategoryNotFoundException].
     *
     * @param e The caught [RuntimeException].
     * @return A [ResponseEntity] containing an [ErrorResponse] and HTTP status 404.
     */
    @ExceptionHandler(
        ProductNotFoundException::class,
        ImageNotFoundException::class,
        CategoryNotFoundException::class,
        CartNotFoundException::class,
        CartItemNotFoundException::class,
        UserNotFoundException::class,
        OrderNotFoundException::class
    )
    fun onNotFoundException(e: RuntimeException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(), message = e.message, error = HttpStatus.NOT_FOUND.reasonPhrase
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    /**
     * Handles exceptions related to illegal or inappropriate arguments (HTTP 400 Bad Request).
     *
     * @param ex The caught [IllegalArgumentException].
     * @return A [ResponseEntity] containing an [ErrorResponse] and HTTP status 400.
     */
    @ExceptionHandler(IllegalArgumentException::class, InsufficientStockException::class)
    fun handleBadRequestException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(), error = HttpStatus.BAD_REQUEST.reasonPhrase, message = ex.message
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    /**
     * Handles exceptions indicating a conflict with the current state of the resource (HTTP 409 Conflict).
     * This includes [ProductAlreadyExistsException] and [CategoryAlreadyExistsException].
     *
     * @param ex The caught [RuntimeException].
     * @return A [ResponseEntity] containing an [ErrorResponse] and HTTP status 409.
     */
    @ExceptionHandler(
        ProductAlreadyExistsException::class,
        CategoryAlreadyExistsException::class,
        UserAlreadyExistsException::class,
    )
    fun handleAlreadyExistsException(ex: RuntimeException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(), error = HttpStatus.CONFLICT.reasonPhrase, message = ex.message
        )
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    /**
     * Handles exceptions occurring during image processing or storage (HTTP 500 Internal Server Error).
     *
     * @param ex The caught [ImageProcessingException].
     * @return A [ResponseEntity] containing an [ErrorResponse] and HTTP status 500.
     */
    @ExceptionHandler(ImageProcessingException::class)
    fun handleImageProcessingException(ex: ImageProcessingException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = ex.message
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * Handles exceptions indicating an illegal state of the application (HTTP 400 Bad Request or 409 Conflict).
     * This is used for cases like trying to delete a category that still has associated products.
     *
     * @param ex The caught [IllegalStateException].
     * @return A [ResponseEntity] containing an [ErrorResponse] and HTTP status 400.
     */
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(), error = HttpStatus.BAD_REQUEST.reasonPhrase, message = ex.message
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    /**
     * Handles errors related to method argument validation (e.g., failed `@Valid` annotations on DTOs).
     * Returns HTTP 400 Bad Request with details about validation errors.
     *
     * @param ex The caught [MethodArgumentNotValidException].
     * @return A [ResponseEntity] containing an [ErrorResponse] and HTTP status 400.
     */
    @ExceptionHandler(MethodArgumentNotValidException::class, InvalidCredentialsException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.map { "${it.field}: ${it.defaultMessage}" }
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(), error = "Validation Error", message = errors.joinToString(", ")
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    /**
     * Handles any other uncaught exceptions (HTTP 500 Internal Server Error).
     * This serves as a fallback for unexpected runtime issues.
     *
     * @param ex The caught [Exception].
     * @return A [ResponseEntity] containing an [ErrorResponse] and HTTP status 500.
     */
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = "An unexpected error occurred. Please try again later."
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}