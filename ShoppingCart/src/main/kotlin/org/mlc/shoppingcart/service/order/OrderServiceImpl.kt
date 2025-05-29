package org.mlc.shoppingcart.service.order

import org.mlc.shoppingcart.dto.order.OrderResponse
import org.mlc.shoppingcart.dto.order.UpdateOrderStatusRequest
import org.mlc.shoppingcart.error.InsufficientStockException
import org.mlc.shoppingcart.error.OrderNotFoundException
import org.mlc.shoppingcart.error.ProductNotFoundException
import org.mlc.shoppingcart.error.UserNotFoundException
import org.mlc.shoppingcart.mapper.toOrderResponse
import org.mlc.shoppingcart.model.Order
import org.mlc.shoppingcart.model.OrderItem
import org.mlc.shoppingcart.model.calculateTotalAmount
import org.mlc.shoppingcart.repository.CartRepository
import org.mlc.shoppingcart.repository.OrderRepository
import org.mlc.shoppingcart.repository.ProductRepository
import org.mlc.shoppingcart.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 * Service implementation for managing order operations in the e-commerce system.
 * Handles the business logic for placing, retrieving, updating, and deleting orders,
 * including interactions with user carts and product inventory.
 */
@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
) : OrderService {

    /**
     * Places a new order for a specified user.
     *
     * This comprehensive operation performs the following steps within a single transaction:
     * 1. Validates the existence of the user and their shopping cart.
     * 2. Checks if the user's cart is empty.
     * 3. Performs an **inventory check** for all products in the cart to ensure sufficient stock.
     * 4. **Decreases the stock** for each product in the order.
     * 5. Creates immutable [OrderItem] snapshots from the current [CartItem]s.
     * 6. Creates a new [Order] entity.
     * 7. Persists the [Order] and associates the [OrderItem]s with it.
     * 8. Calculates and updates the [Order]'s total amount.
     * 9. Clears the user's shopping cart.
     *
     * @param userId The unique identifier of the user placing the order.
     * @return An [OrderResponse] DTO representing the newly placed order.
     * @throws UserNotFoundException If the specified user does not exist.
     * @throws IllegalStateException If the user does not have a cart or if their cart is empty.
     * @throws ProductNotFoundException If a product referenced in the cart is not found in the inventory.
     * @throws InsufficientStockException If there is not enough stock for any product in the cart.
     */
    @Transactional
    override fun placeOrder(userId: Long): OrderResponse {

        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("User not exists")

        val userCart = user.cart
            ?: throw IllegalStateException("User does not have cart")

        if (userCart.cartItems.isEmpty()) throw IllegalStateException("Cannot place an order with an empty cart")

        //  INVENTORY CHECK
        userCart.cartItems.forEach { cartItem ->
            val product = productRepository.findByIdOrNull(cartItem.product.id)
                ?: throw ProductNotFoundException("Product with ID ${cartItem.product.id} in cart not found.")
            if (product.stock < cartItem.quantity) {
                throw InsufficientStockException("Insufficient stock for product ${product.name}. Available: ${product.stock}, Requested: ${cartItem.quantity}.")
            }
        }

        //  UPDATE STOCK
        userCart.cartItems.forEach { cartItem ->
            cartItem.product.stock -= cartItem.quantity
            productRepository.save(cartItem.product)
        }


        val orderItems = userCart.cartItems.map { cartItem ->
            OrderItem(
                product = cartItem.product,
                quantity = cartItem.quantity,
                unitPrice = cartItem.unitPrice,
                order = null, // Will be set after order is saved and gets an ID
            )
        }.toMutableSet()

        val newOrder = Order(user = user) // Initialize order with user

        val savedOrder = orderRepository.save(newOrder) // Save order to get its generated ID
        orderItems.forEach { it.order = savedOrder } // Associate order items with the saved order
        savedOrder.orderItems.addAll(orderItems) // Add order items to the order's collection
        savedOrder.calculateTotalAmount() // Calculate and set the total amount for the order

        orderRepository.save(savedOrder) // Save the order again to persist order items and total amount

        userCart.cartItems.clear() // Clear user's cart
        cartRepository.save(userCart) // Persist the empty cart (orphanRemoval will delete CartItems)

        return savedOrder.toOrderResponse()
    }

    /**
     * Retrieves a specific order by its unique identifier.
     *
     * @param orderId The unique identifier of the order to retrieve.
     * @return An [OrderResponse] DTO representing the retrieved order.
     * @throws OrderNotFoundException If no order with the given ID is found.
     */
    @Transactional(readOnly = true)
    override fun getOrderById(orderId: Long): OrderResponse {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException("Order with ID $orderId not found.")
        return order.toOrderResponse()
    }

    /**
     * Updates an existing order's status.
     * This method is typically used by internal systems or administrators to manage the order's lifecycle.
     *
     * @param orderId The unique identifier of the order to update.
     * @param request An [UpdateOrderStatusRequest] DTO containing the new status.
     * @return An [OrderResponse] DTO representing the updated order.
     * @throws OrderNotFoundException If no order with the given ID is found.
     */
    @Transactional
    override fun updateOrderById(orderId: Long, request: UpdateOrderStatusRequest): OrderResponse {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException("Order with ID $orderId not found.")

        order.status = request.status
        orderRepository.save(order)
        return order.toOrderResponse()
    }

    /**
     * Deletes a specific order by its unique identifier.
     *
     * This operation will trigger cascading deletions of associated [OrderItem]s
     * due to the `CascadeType.ALL` and `orphanRemoval = true` configuration on the
     * `orderItems` collection in the [Order] entity.
     *
     * @param orderId The unique identifier of the order to delete.
     * @throws OrderNotFoundException If no order with the given ID is found.
     */
    @Transactional
    override fun deleteOrderById(orderId: Long) {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw OrderNotFoundException("Order with ID $orderId not found.")

        orderRepository.delete(order)
    }
}