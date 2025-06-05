package org.mlc.shoppingcart.service.user

import org.mlc.shoppingcart.dto.user.CreateUserRequest
import org.mlc.shoppingcart.dto.user.UpdateUserRequest
import org.mlc.shoppingcart.dto.user.UserResponse

interface UserService {
    fun registerNewUser(request: CreateUserRequest): UserResponse
    fun updateUserProfile(request: UpdateUserRequest, userId: Long): UserResponse
    fun deleteUser(userId: Long)
    fun getUserById(userId: Long): UserResponse
    fun getUserByEmail(email: String): UserResponse
}