package org.mlc.shoppingcart.error

import java.time.Instant

data class ErrorResponse(
    val timestamp: Instant = Instant.now(),
    val status: Int,
    val message: String?,
    val error: String
)
