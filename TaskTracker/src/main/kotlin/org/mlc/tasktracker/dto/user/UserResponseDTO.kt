package org.mlc.tasktracker.dto.user


/** Data Transfer Object (DTO) for representing a user in API responses.
  * This structure is used when returning user details to the client,
  * excluding sensitive information like the password.
  *
  * @property id The unique identifier of the user.
  * @property userName The username of the user.
 */

data class UserResponseDTO(
  val id: String,
  val userName: String
)