package org.mlc.tasktracker.dto.user

/**
 * Data Transfer Object (DTO) for user registration.
 * This structure is expected in the request body when a new user signs up.
 *
 * @property userName The desired username for the new user.
 * @property password The desired password for the new user.
 */
data class UserRegistrationRequestDTO(
  val userName: String,
  val password: String
)
