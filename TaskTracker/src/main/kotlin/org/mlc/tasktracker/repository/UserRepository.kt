package org.mlc.tasktracker.repository

import org.mlc.tasktracker.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the [User] entity.
 * This interface provides out-of-the-box CRUD operations and custom query methods.
 */
@Repository
interface UserRepository : JpaRepository<User, String> {

    /**
     * Finds a user by their username.
     *
     * @param userName The username to search for.
     * @return The [User] if found, or `null` otherwise.
     */
    fun findByUserName(userName: String): User?

    /**
     * Checks if a user exists with the given username.
     *
     * @param userName The username to check for.
     * @return True if a user with the given username exists, false otherwise.
     */
    fun existsByUserName(userName: String): Boolean // Corregido el nombre del método

}