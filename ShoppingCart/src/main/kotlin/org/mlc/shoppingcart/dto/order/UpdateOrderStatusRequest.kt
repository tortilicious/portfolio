package org.mlc.shoppingcart.dto.order

import jakarta.validation.constraints.NotNull // Make sure this is imported
import org.mlc.shoppingcart.utils.OrderStatus // Make sure this is imported

/**
 * Data Transfer Object (DTO) for updating the status of an existing order.
 *
 * This DTO specifically targets the modification of an order's lifecycle status.
 * It's intended to be used as a request body for API endpoints updating order status.
 */
data class UpdateOrderStatusRequest(

    @field:NotNull(message = "Order status cannot be null for an update")
    val status: OrderStatus
)