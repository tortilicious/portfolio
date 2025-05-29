package org.mlc.shoppingcart.service.order

import org.mlc.shoppingcart.dto.order.OrderResponse
import org.mlc.shoppingcart.dto.order.UpdateOrderStatusRequest

/**
 * Defines the contract for managing order-related operations in the e-commerce system.
 * This interface provides methods for placing new orders, retrieving existing ones,
 * updating order statuses, and deleting orders.
 */
interface OrderService {
    /**
     * Places a new order for a specified user based on the contents of their shopping cart.
     * This operation includes inventory checks, stock updates, order item creation,
     * total amount calculation, and clearing the user's cart.
     *
     * @param userId The unique identifier of the user placing the order.
     * @return An [OrderResponse] DTO representing the newly placed order.
     * @throws UserNotFoundException If the specified user does not exist.
     * @throws IllegalStateException If the user does not have a cart or if their cart is empty.
     * @throws ProductNotFoundException If a product in the cart is not found in the inventory.
     * @throws InsufficientStockException If there is not enough stock for any product in the cart.
     */
    fun placeOrder(userId: Long): OrderResponse

    /**
     * Retrieves a specific order by its unique identifier.
     *
     * @param orderId The unique identifier of the order to retrieve.
     * @return An [OrderResponse] DTO representing the retrieved order.
     * @throws OrderNotFoundException If no order with the given ID is found.
     */
    fun getOrderById(orderId: Long): OrderResponse

    /**
     * Updates the status of an existing order. This is typically used for
     * managing the order's lifecycle (e.g., from PENDING to SHIPPED).
     *
     * @param orderId The unique identifier of the order to update.
     * @param request An [UpdateOrderStatusRequest] DTO containing the new status.
     * @return An [OrderResponse] DTO representing the updated order.
     * @throws OrderNotFoundException If no order with the given ID is found.
     */
    fun updateOrderById(orderId: Long, request: UpdateOrderStatusRequest): OrderResponse

    /**
     * Deletes a specific order by its unique identifier.
     *
     * This operation typically triggers cascading deletions for associated order items.
     * Depending on business rules, orders might be soft-deleted or archived instead of
     * permanently removed in a production environment.
     *
     * @param orderId The unique identifier of the order to delete.
     * @throws OrderNotFoundException If no order with the given ID is found.
     */
    fun deleteOrderById(orderId: Long)
}