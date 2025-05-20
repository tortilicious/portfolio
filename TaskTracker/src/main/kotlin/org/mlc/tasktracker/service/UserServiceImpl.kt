package org.mlc.tasktracker.service

import org.mlc.tasktracker.dto.user.UserLoginRequestDTO
import org.mlc.tasktracker.dto.user.UserRegistrationRequestDTO
import org.mlc.tasktracker.dto.user.UserResponseDTO
import org.mlc.tasktracker.exception.AuthorizationException
import org.mlc.tasktracker.exception.UserAlreadyExistException
import org.mlc.tasktracker.exception.UserNotFoundException
import org.mlc.tasktracker.model.User
import org.mlc.tasktracker.repository.UserRepository
import org.mlc.tasktracker.service.mapper.toResponseDTO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    /**
     * Registers a new user in the system.
     *
     * This method performs the following steps:
     * 1. Checks if a user with the provided username already exists in the database.
     * 2. If a user with the username already exists, it throws a [UserAlreadyExistException].
     * 3. If the username is unique, it hashes the provided password using the [PasswordEncoder].
     * 4. Creates a new [User] entity with a randomly generated UUID as the ID,
     * the unique username, the hashed password, and an empty list of task lists.
     * 5. Saves the new user entity to the database.
     * 6. Converts the saved [User] entity to a [UserResponseDTO] and returns it.
     *
     * The operation is transactional, ensuring atomicity; if any part of the registration
     * process fails, the entire transaction will be rolled back.
     *
     * @param registrationRequestDTO The [UserRegistrationRequestDTO] containing the new user's
     * username and plain-text password.
     * @return A [UserResponseDTO] representing the newly registered user, including
     * their unique ID and username.
     * @throws UserAlreadyExistException if a user with the provided username already exists.
     * @see User
     * @see UserRegistrationRequestDTO
     * @see UserResponseDTO
     * @see UserRepository
     * @see PasswordEncoder
     */
    @Transactional
    override fun registerUser(registrationRequestDTO: UserRegistrationRequestDTO): UserResponseDTO {

        val user = userRepository.findByUserName(registrationRequestDTO.userName)
        user?.let {
            throw UserAlreadyExistException("Username '${registrationRequestDTO.userName}' is already taken.")
        }

        val newUserHashedPassword = passwordEncoder.encode(registrationRequestDTO.password)
        val newUser = User(
            id = UUID.randomUUID().toString(),
            userName = registrationRequestDTO.userName,
            password = newUserHashedPassword,
            taskLists = mutableListOf()
        )

        val savedUser = userRepository.save(newUser)
        return savedUser.toResponseDTO()
    }

    /**
     * Authenticates a user by verifying their username and password.
     *
     * This method attempts to find a user by the provided username. If found, it then
     * securely compares the provided plain-text password with the stored hashed password
     * using [PasswordEncoder.matches].
     *
     * @param loginRequestDTO The [UserLoginRequestDTO] containing the user's username and password.
     * @return `true` if the username and password match, indicating successful authentication.
     * @throws AuthorizationException if the username is not found or the provided password
     * does not match the stored password (invalid credentials).
     * @see User
     * @see UserLoginRequestDTO
     * @see UserRepository
     * @see PasswordEncoder
     */
    @Transactional(readOnly = true)
    override fun loginUser(loginRequestDTO: UserLoginRequestDTO): Boolean {
        val user = userRepository.findByUserName(loginRequestDTO.userName)
        return user?.let {
            if (passwordEncoder.matches(loginRequestDTO.password, it.password)) {
                true
            } else {
                throw AuthorizationException("Invalid credentials")
            }
        } ?: throw AuthorizationException("Invalid credentials")
    }

    /**
     * Retrieves a user's details by their username.
     *
     * This method attempts to find a user by the specified username. If no user is found,
     * a [UserNotFoundException] is thrown.
     *
     * @param username The username of the user to retrieve.
     * @return A [UserResponseDTO] containing the details of the found user.
     * @throws UserNotFoundException if a user with the specified username cannot be found.
     * @see User
     * @see UserResponseDTO
     * @see UserRepository
     */
    @Transactional(readOnly = true)
    override fun getUserByUsername(username: String): UserResponseDTO {
        val user = userRepository.findByUserName(username)
            ?: throw UserNotFoundException("Cant find user by username '$username'")

        return user.toResponseDTO()
    }

    /**
     * Retrieves a user's details by their unique identifier (ID).
     *
     * This method attempts to find a user by the specified ID. It utilizes Spring Data JPA's
     * `findByIdOrNull` for concise handling of optional results. If no user is found,
     * a [UserNotFoundException] is thrown.
     *
     * @param id The unique ID of the user to retrieve.
     * @return A [UserResponseDTO] containing the details of the found user.
     * @throws UserNotFoundException if a user with the specified ID cannot be found.
     * @see User
     * @see UserResponseDTO
     * @see UserRepository
     */
    @Transactional(readOnly = true)
    override fun getUserById(id: String): UserResponseDTO {
        val user = userRepository.findByIdOrNull(id)
            ?: throw UserNotFoundException("Cant find user by id '$id'")
        return user.toResponseDTO()
    }

    /**
     * Changes a user's password after verifying the old password.
     *
     * This method performs the following steps:
     * 1. Retrieves the user by their unique ID. If the user is not found, it throws a [UserNotFoundException].
     * 2. **Verifies the provided `oldPassword`** against the user's current stored hashed password
     * using [PasswordEncoder.matches]. If the `oldPassword` is incorrect, an [AuthorizationException] is thrown.
     * 3. Hashes the `newPassword` using the [PasswordEncoder].
     * 4. Updates the user's password with the newly hashed password.
     * 5. Saves the updated user entity to the database.
     * 6. Converts the updated [User] entity to a [UserResponseDTO] and returns it.
     *
     * The operation is transactional, ensuring atomicity.
     *
     * @param userId The ID of the user whose password is to be changed.
     * @param oldPassword The user's current plain-text password for verification.
     * @param newPassword The new plain-text password to set for the user.
     * @return A [UserResponseDTO] representing the user with the updated password.
     * @throws UserNotFoundException if the user with the specified `userId` cannot be found.
     * @throws AuthorizationException if the provided `oldPassword` does not match the user's current password.
     * @see User
     * @see UserResponseDTO
     * @see UserRepository
     * @see PasswordEncoder
     */
    @Transactional
    override fun changePassword(userId: String, oldPassword: String, newPassword: String): UserResponseDTO {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("Cant find user by id '$userId'")

        if (!passwordEncoder.matches(oldPassword, user.password)) {
            throw AuthorizationException("Invalid credentials")
        }
        val newUserHashedPassword = passwordEncoder.encode(newPassword)
        user.password = newUserHashedPassword
        userRepository.save(user)
        return user.toResponseDTO()
    }
}