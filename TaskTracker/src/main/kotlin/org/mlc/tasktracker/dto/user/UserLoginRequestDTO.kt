package org.mlc.tasktracker.dto.user

/**
 * Data Transfer Object (DTO) for user login.
 * This structure is expected in the request body when a user attempts to log in.
 *
 * @property userName The username of the user trying to log in.
 * @property password The password of the user trying to log in.
 */
data class UserLoginRequestDTO(
    val userName: String,
    val password: String
)
