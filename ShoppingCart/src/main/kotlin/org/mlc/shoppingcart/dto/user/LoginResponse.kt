package org.mlc.shoppingcart.dto.user

data class LoginResponse(
    val token: String,
    val id: Long,
    val email: String,
    val roles: List<String>
)
