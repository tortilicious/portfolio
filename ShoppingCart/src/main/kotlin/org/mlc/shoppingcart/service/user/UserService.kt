package org.mlc.shoppingcart.service.user

import org.mlc.shoppingcart.dto.user.*

interface UserService {
    fun registerNewUser(request: CreateUserRequest): UserResponse
    fun userLogin(request: LoginRequest): LoginResponse
    fun updateUserProfile(request: UpdateUserRequest, userId: Long): UserResponse
    fun deleteUser(userId: Long)
    fun getUserById(userId: Long): UserResponse
    fun getUserByEmail(email: String): UserResponse
}