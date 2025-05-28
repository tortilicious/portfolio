package org.mlc.shoppingcart.dto.user

data class LoginResponse(
    val id: Long,
    val email: String,
    val message: String
)
