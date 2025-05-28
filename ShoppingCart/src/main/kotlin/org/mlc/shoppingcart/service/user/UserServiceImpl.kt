package org.mlc.shoppingcart.service.user


import org.mlc.shoppingcart.dto.user.*
import org.mlc.shoppingcart.error.InvalidCredentialsException
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


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {


    @Transactional
    override fun registerNewUser(request: CreateUserRequest): UserResponse {
        val user = userRepository.findByEmail(request.email)

        if (user != null) throw UserAlreadyExistsException("User already exists")

        val hashedPassword = passwordEncoder.encode(request.password)
        val newUser = User(
            email = request.email,
            password = hashedPassword,
            firstName = request.firstName,
            lastName = request.lastName
        )
        val savedUser = userRepository.save(newUser)

        val newCart = Cart(user = savedUser)
        val savedCart = cartRepository.save(newCart)

        savedUser.cart = savedCart
        userRepository.save(savedUser)

        return savedUser.toUserResponse()

    }

    @Transactional(readOnly = true)
    override fun userLogin(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw UserNotFoundException("User does not exists")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw InvalidCredentialsException("Invalid credentials")
        } else {
            return LoginResponse(
                id = user.id,
                email = user.email,
                message = "Login successful"
            )
        }
    }


    @Transactional
    override fun updateUserProfile(request: UpdateUserRequest, userId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(userId)

        if (user != null) {
            request.email?.let { user.email = it }
            request.password?.let { user.password = passwordEncoder.encode(it) }
            request.firstName?.let { user.firstName = it }
            request.lastName?.let { user.lastName = it }

            val savedUser = userRepository.save(user)
            return savedUser.toUserResponse()
        }
        throw UserNotFoundException("User not found")
    }

    @Transactional
    override fun deleteUser(userId: Long) {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("User not found")
        userRepository.delete(user)
    }

    override fun getUserById(userId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("User not found")
        return user.toUserResponse()
    }

    override fun getUserByEmail(email: String): UserResponse {
        val user = userRepository.findByEmail(email)
            ?: throw UserNotFoundException("User not found")
        return user.toUserResponse()
    }

}