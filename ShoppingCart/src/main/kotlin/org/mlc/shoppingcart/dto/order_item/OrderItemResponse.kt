package org.mlc.shoppingcart.dto.order_item

import java.math.BigDecimal

/**
 * Data Transfer Object (DTO) representing an individual item within an order.
 *
 * This DTO provides a snapshot of a product at the time of order,
 * including its identifier, name, quantity, and total price for that item.
 */
data class OrderItemResponse(
    val id: Long,
    val productId: Long,
    val productName: String,
    val productQuantity: Int,
    val totalPrice: BigDecimal
)
