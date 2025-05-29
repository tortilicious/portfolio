package org.mlc.shoppingcart.dto.order

import org.mlc.shoppingcart.dto.order_item.OrderItemResponse
import org.mlc.shoppingcart.utils.OrderStatus // Make sure this is imported
import java.math.BigDecimal
import java.time.Instant

/**
 * Data Transfer Object (DTO) representing the response for an order.
 *
 * This DTO provides a comprehensive view of an order, including its unique identifier,
 * current status, user details, date, total amount, and a list of associated items.
 * It's designed to be sent back to the client via API responses.
 */
data class OrderResponse(
    val id: Long,
    val status: OrderStatus,
    val email: String,
    val firstName: String,
    val lastName: String,
    val date: Instant,
    val totalAmount: BigDecimal,
    val items: MutableSet<OrderItemResponse>
)