package org.mlc.tasktracker.service

import org.mlc.tasktracker.dto.user.UserLoginRequestDTO
import org.mlc.tasktracker.dto.user.UserRegistrationRequestDTO
import org.mlc.tasktracker.dto.user.UserResponseDTO

/**
 * Service interface for managing User entities and user-related operations.
 * Defines the contract for business logic related to user registration, login,
 * data retrieval, and password management.
 * The implementing class will also typically integrate with Spring Security's `UserDetailsService`
 * for authentication purposes.
 */
interface UserService {

  /**
   * Registers a new user in the system.
   * This involves validating registration data, checking for username uniqueness,
   * hashing the password, and persisting the new user.
   *
   * @param registrationRequestDTO The DTO containing the new user's registration details (username and password).
   * @return A [UserResponseDTO] representing the newly registered user, excluding sensitive data.
   * @throws UserAlreadyExistsException if a user with the provided username already exists.
   * @throws IllegalArgumentException if registration data is invalid (e.g., blank username, weak password).
   */
  fun registerUser(registrationRequestDTO: UserRegistrationRequestDTO): UserResponseDTO

  /**
   * Attempts to log in a user with the provided credentials.
   * Note: In a typical JWT-based authentication setup, a successful login would return an
   * authentication token (e.g., wrapped in an AuthResponseDTO) rather than a simple boolean.
   * This method's signature might evolve when Spring Security JWT is implemented.
   *
   * @param loginRequestDTO The DTO containing the user's login credentials (username and password).
   * @return `true` if the login credentials are valid; `false` otherwise.
   * @throws UserNotFoundException if the user with the provided username does not exist.
   * @throws InvalidCredentialsException if the password does not match for the given username.
   */
  fun loginUser(loginRequestDTO: UserLoginRequestDTO): Boolean

  /**
   * Retrieves user details by their username.
   * Excludes sensitive information like the password.
   *
   * @param username The username of the user to find.
   * @return A [UserResponseDTO] if a user with the given username is found; otherwise, `null`.
   */
  fun getUserByUsername(username: String): UserResponseDTO?

  /**
   * Retrieves user details by their unique ID.
   * Excludes sensitive information like the password.
   *
   * @param id The unique identifier of the user to find.
   * @return A [UserResponseDTO] if a user with the given ID is found; otherwise, `null`.
   */
  fun getUserById(id: String): UserResponseDTO?

  /**
   * Changes the password for a specified user after verifying the old password.
   *
   * @param userId The unique identifier of the user whose password is to be changed.
   * @param oldPassword The user's current (old) password for verification.
   * @param newPassword The new password to set.
   * @return A [UserResponseDTO] representing the user if the password change was successful;
   * `null` if the user is not found, the old password does not match, or the new password is invalid.
   * @throws UserNotFoundException if the user with the specified [userId] is not found.
   * @throws InvalidCredentialsException if the [oldPassword] does not match the user's current password.
   * @throws IllegalArgumentException if the [newPassword] does not meet complexity requirements.
   */
  fun changePassword(userId: String, oldPassword: String, newPassword: String): UserResponseDTO?
}