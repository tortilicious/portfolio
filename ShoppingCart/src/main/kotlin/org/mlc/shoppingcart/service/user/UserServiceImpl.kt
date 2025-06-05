package org.mlc.shoppingcart.service.user

import org.mlc.shoppingcart.dto.user.*
// Ya no necesitas InvalidCredentialsException para el login aquí
import org.mlc.shoppingcart.error.UserAlreadyExistsException
import org.mlc.shoppingcart.error.UserNotFoundException
import org.mlc.shoppingcart.mapper.toUserResponse
import org.mlc.shoppingcart.model.Cart
import org.mlc.shoppingcart.model.User
import org.mlc.shoppingcart.repository.CartRepository
import org.mlc.shoppingcart.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service // Asegúrate que la implementación tenga @Service, no la interfaz UserService.
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService { // Asumo que tienes una interfaz UserService

    @Transactional
    override fun registerNewUser(request: CreateUserRequest): UserResponse {
        userRepository.findByEmail(request.email)?.let {
            throw UserAlreadyExistsException("User with email ${request.email} already exists")
        }

        val hashedPassword = passwordEncoder.encode(request.password)
        val newUser = User(
            email = request.email,
            password = hashedPassword,
            firstName = request.firstName,
            lastName = request.lastName
        )
        val savedUser = userRepository.save(newUser)

        // Crear y asociar carrito
        val newCart = Cart(user = savedUser)
        cartRepository.save(newCart)
        return savedUser.toUserResponse()
    }


    @Transactional
    override fun updateUserProfile(request: UpdateUserRequest, userId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("User not found with ID: $userId")

        request.email?.let { user.email = it }
        request.password?.let { user.password = passwordEncoder.encode(it) }
        request.firstName?.let { user.firstName = it }
        request.lastName?.let { user.lastName = it }

        val savedUser = userRepository.save(user)
        return savedUser.toUserResponse()
    }

    @Transactional
    override fun deleteUser(userId: Long) {
        if (!userRepository.existsById(userId)) {
            throw UserNotFoundException("User not found with ID: $userId")
        }
        userRepository.deleteById(userId)
    }

    @Transactional(readOnly = true)
    override fun getUserById(userId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("User not found with ID: $userId")
        return user.toUserResponse()
    }

    @Transactional(readOnly = true)
    override fun getUserByEmail(email: String): UserResponse {
        val user = userRepository.findByEmail(email)
            ?: throw UserNotFoundException("User not found with email: $email")
        return user.toUserResponse()
    }
}