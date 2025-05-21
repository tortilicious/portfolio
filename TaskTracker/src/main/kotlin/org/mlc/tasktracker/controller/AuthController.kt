package org.mlc.tasktracker.controller

import org.mlc.tasktracker.dto.user.UserChangePasswordRequest
import org.mlc.tasktracker.dto.user.UserLoginRequestDTO
import org.mlc.tasktracker.dto.user.UserRegistrationRequestDTO
import org.mlc.tasktracker.dto.user.UserResponseDTO
import org.mlc.tasktracker.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


/**
 * REST controller for handling user authentication and registration.
 * This controller provides endpoints for user registration, login, and password changes.
 *
 * @property userService The service layer for user-related business logic, including authentication.
 */
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService
) {

    /**
     * Registers a new user in the system.
     *
     * This endpoint handles HTTP POST requests to `/api/auth/register`.
     * It expects a [UserRegistrationRequestDTO] in the request body containing the new user's details.
     *
     * @param registrationRequestDTO The [UserRegistrationRequestDTO] containing the new user's
     * username and plain-text password.
     * @return A [ResponseEntity] containing the [UserResponseDTO] of the newly registered user
     * and an HTTP status of [HttpStatus.CREATED].
     * @throws org.mlc.tasktracker.exception.UserAlreadyExistException if a user with the provided username already exists.
     * @see UserService.registerUser
     * @see UserRegistrationRequestDTO
     * @see UserResponseDTO
     */
    @PostMapping("/register")
    fun registerUser(@RequestBody registrationRequestDTO: UserRegistrationRequestDTO): ResponseEntity<UserResponseDTO> {
        val registratedUser = userService.registerUser(registrationRequestDTO)
        return ResponseEntity(registratedUser, HttpStatus.CREATED)

    }

    /**
     * Authenticates a user by verifying their username and password.
     *
     * This endpoint handles HTTP POST requests to `/api/auth/login`.
     * It expects a [UserLoginRequestDTO] in the request body containing the user's credentials.
     *
     * @param loginRequestDTO The [UserLoginRequestDTO] containing the user's username and password.
     * @return A [ResponseEntity] containing `true` if authentication is successful,
     * and an HTTP status of [HttpStatus.OK].
     * @throws org.mlc.tasktracker.exception.AuthorizationException if the username is not found or the provided password
     * does not match the stored password (invalid credentials).
     * @see UserService.loginUser
     * @see UserLoginRequestDTO
     */
    @PostMapping("/login")
    fun loginUser(@RequestBody loginRequestDTO: UserLoginRequestDTO): ResponseEntity<Boolean> {
        val userLogin = userService.loginUser(loginRequestDTO)
        return ResponseEntity(userLogin, HttpStatus.OK)
    }

    /**
     * Allows an authenticated user to change their password.
     *
     * This endpoint handles HTTP POST requests to `/api/auth/change-password`.
     * It requires the user's ID in the `X-User-ID` request header and a
     * [UserChangePasswordRequest] in the request body, which includes the old and new passwords.
     * The `oldPassword` is verified against the stored password before the change is applied.
     *
     * @param userId The ID of the user requesting the password change, obtained from the "X-User-ID" header.
     * @param changePasswordRequest The [UserChangePasswordRequest] containing the old and new plain-text passwords.
     * @return A [ResponseEntity] containing the [UserResponseDTO] of the user with the updated password
     * and an HTTP status of [HttpStatus.OK].
     * @throws org.mlc.tasktracker.exception.UserNotFoundException if the user with the specified `userId` cannot be found.
     * @throws org.mlc.tasktracker.exception.AuthorizationException if the provided `oldPassword` does not match the user's current password.
     * @see UserService.changePassword
     * @see UserChangePasswordRequest
     * @see UserResponseDTO
     */
    @PostMapping("/change-password")
    fun changePassword(
        @RequestHeader("X-User-ID") userId: String,
        @RequestBody changePasswordRequest: UserChangePasswordRequest
    ): ResponseEntity<UserResponseDTO> {
        val changePasswordResponse = userService.changePassword(userId, changePasswordRequest.oldPassword, changePasswordRequest.newPassword)
        return ResponseEntity(changePasswordResponse, HttpStatus.OK)
    }
}