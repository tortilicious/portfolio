package org.mlc.shoppingcart.dto.cart_item

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class AddItemToCartRequest(

    @field:NotNull(message = "Product ID cannot be null")
    val productId: Long,

    @field:Min(value = 1, message = "Quantity must be at least 1")
    val quantity: Int
)