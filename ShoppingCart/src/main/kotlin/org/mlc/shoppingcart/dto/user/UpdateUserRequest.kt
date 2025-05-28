package org.mlc.shoppingcart.dto.user

data class UpdateUserRequest (
    val email: String?,
    val password: String?,
    val firstName: String?,
    val lastName: String?
)