package org.mlc.shoppingcart.dto.cart

import org.mlc.shoppingcart.dto.cart_item.CartItemResponse
import java.math.BigDecimal

/**
 * Data Transfer Object (DTO) representing a shopping cart for client-side consumption.
 * This object is used to send cart details from the backend to the frontend or other services,
 * providing a simplified view of the [org.mlc.shoppingcart.model.Cart] entity.
 */
data class CartResponse(
    /**
     * The unique identifier of the cart.
     */
    val id: Long,

    /**
     * The total monetary amount of all items in the cart.
     * This value is precisely represented using [BigDecimal].
     */
    var totalAmount: BigDecimal,

    /**
     * A list of [CartItemResponse] objects representing the individual items within the cart.
     */
    var cartItems: List<CartItemResponse>
)