package org.mlc.tasktracker.dto.user

/**
 * Data Transfer Object (DTO) for authentication responses.
 * This structure is returned to the client after a successful login, containing
 * an authentication token.
 *
 * @property userName The username of the authenticated user.
 * @property token The JSON Web Token (JWT) or other authentication token for the session.
 */
data class AuthResponseDTO(
  val userName: String,
  val token: String
)
