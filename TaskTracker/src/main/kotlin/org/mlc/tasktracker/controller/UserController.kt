package org.mlc.tasktracker.controller

import org.mlc.tasktracker.dto.user.UserResponseDTO
import org.mlc.tasktracker.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


/**
 * REST controller for managing user-related queries.
 * This controller provides endpoints to retrieve user information.
 *
 * @property userService The service layer for user-related business logic.
 */
@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    /**
     * Fetches a user's details by their username.
     *
     * This endpoint handles HTTP GET requests to `/api/users/search`.
     * The username is provided as a request parameter.
     *
     * @param userName The username of the user to search for, obtained from the "username" request parameter.
     * @return A [ResponseEntity] containing the [UserResponseDTO] of the found user
     * and an HTTP status of [HttpStatus.OK].
     * @throws org.mlc.tasktracker.exception.UserNotFoundException if a user with the specified username cannot be found.
     * @see UserService.getUserByUsername
     * @see UserResponseDTO
     */
    @GetMapping("/search")
    fun fetchUserByName(@RequestParam("username") userName: String): ResponseEntity<UserResponseDTO> {
        val user = userService.getUserByUsername(userName)
        return ResponseEntity(user, HttpStatus.OK)
    }

    /**
     * Fetches a user's details by their unique identifier (ID).
     *
     * This endpoint handles HTTP GET requests to `/api/users/id/{userId}`.
     * The user ID is provided as a path variable.
     *
     * @param userId The unique ID of the user to fetch, extracted from the path variable.
     * @return A [ResponseEntity] containing the [UserResponseDTO] of the found user
     * and an HTTP status of [HttpStatus.OK].
     * @throws org.mlc.tasktracker.exception.UserNotFoundException if a user with the specified ID cannot be found.
     * @see UserService.getUserById
     * @see UserResponseDTO
     */
    @GetMapping("/id/{userId}")
    fun fetchUserById(@PathVariable userId: String): ResponseEntity<UserResponseDTO> {
        val user = userService.getUserById(userId)
        return ResponseEntity(user, HttpStatus.OK)
    }
}