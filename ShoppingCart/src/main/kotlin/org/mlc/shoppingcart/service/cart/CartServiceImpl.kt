package org.mlc.shoppingcart.service.cart

import org.mlc.shoppingcart.dto.cart.CartResponse
import org.mlc.shoppingcart.error.CartItemNotFoundException
import org.mlc.shoppingcart.error.CartNotFoundException
import org.mlc.shoppingcart.error.ProductNotFoundException
import org.mlc.shoppingcart.error.UserNotFoundException
import org.mlc.shoppingcart.mapper.toCartResponse
import org.mlc.shoppingcart.model.Cart
import org.mlc.shoppingcart.model.CartItem
import org.mlc.shoppingcart.model.updateOverallTotalAmount
import org.mlc.shoppingcart.model.updateTotalPrice
import org.mlc.shoppingcart.repository.CartRepository
import org.mlc.shoppingcart.repository.ProductRepository
import org.mlc.shoppingcart.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/**
 * Service implementation for managing shopping cart operations.
 * Provides methods for retrieving, manipulating, and persisting cart data.
 */
@Service
class CartServiceImp(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository, // CartItemRepository is not directly used for common operations
    private val userRepository: UserRepository
) : CartService {

    /**
     * Retrieves a shopping cart by its unique ID.
     *
     * @param id The ID of the cart to retrieve.
     * @return A [CartResponse] representing the retrieved cart.
     * @throws CartNotFoundException if no cart with the given ID is found.
     */
    @Transactional(readOnly = true)
    override fun getCart(id: Long): CartResponse {
        val cart = cartRepository.findByIdOrNull(id)
            ?: throw CartNotFoundException("Could not find cart with id: $id")
        return cart.toCartResponse()
    }

    /**
     * Clears all items from a specified shopping cart.
     *
     * @param id The ID of the cart to clear.
     * @throws CartNotFoundException if no cart with the given ID is found.
     */
    @Transactional
    override fun clearCart(id: Long) {
        val cart = cartRepository.findByIdOrNull(id)
            ?: throw CartNotFoundException("Could not find cart with id: $id")

        cart.cartItems.clear() // Removes all items from the collection, triggering orphanRemoval
        cart.updateOverallTotalAmount() // Recalculates total amount (should become zero)
        cartRepository.save(cart) // Persists the changes to the cart
    }

    /**
     * Calculates and retrieves the total price of a shopping cart.
     * This method returns the pre-calculated total amount stored in the cart entity.
     *
     * @param id The ID of the cart.
     * @return A [BigDecimal] representing the total price of the cart.
     * @throws CartNotFoundException if no cart with the given ID is found.
     */
    @Transactional(readOnly = true)
    override fun getTotalPrice(id: Long): BigDecimal {
        val cart = cartRepository.findByIdOrNull(id)
            ?: throw CartNotFoundException("Could not find cart with id: $id")
        return cart.totalAmount
    }



    /**
     * Adds a specified quantity of a product to a shopping cart.
     * If the product already exists in the cart, its quantity is updated; otherwise, a new cart item is created.
     *
     * @param cartId The ID of the cart to add the product to.
     * @param productId The ID of the product to add.
     * @param quantity The quantity of the product to add.
     * @return A [CartResponse] representing the updated cart.
     * @throws CartNotFoundException if the specified cart is not found.
     * @throws ProductNotFoundException if the specified product is not found.
     */
    @Transactional
    override fun addProduct(cartId: Long, productId: Long, quantity: Int): CartResponse {
        val cart = cartRepository.findByIdOrNull(cartId)
            ?: throw CartNotFoundException("Could not find cart with id: $cartId")

        val product = productRepository.findByIdOrNull(productId)
            ?: throw ProductNotFoundException("Could not find product with id: $productId")

        val existingCartItem = cart.cartItems.find { it.product.id == productId }

        existingCartItem?.also {
            it.quantity += quantity
            it.updateTotalPrice()
        } ?: run {
            val newCartItem = CartItem(
                product = product,
                quantity = quantity,
                unitPrice = product.price,
                totalPrice = BigDecimal.ZERO, // Initialized to zero, updated by updateTotalPrice()
                cart = cart
            )
            newCartItem.updateTotalPrice()
            cart.cartItems.add(newCartItem) // Add the new item to the cart's collection
        }

        cart.updateOverallTotalAmount() // Recalculate cart's total
        val updatedCart = cartRepository.save(cart) // Save all changes to the cart and its items
        return updatedCart.toCartResponse()
    }

    /**
     * Updates the quantity of a specific product within a shopping cart.
     * If the new quantity is zero or less, the item will be removed from the cart.
     *
     * @param cartId The ID of the cart containing the item.
     * @param productId The ID of the product whose quantity needs to be updated.
     * @param newQuantity The new quantity for the product.
     * @return A [CartResponse] representing the updated cart.
     * @throws CartNotFoundException if the specified cart is not found.
     * @throws CartItemNotFoundException if the specified product is not found in the cart.
     */
    @Transactional
    override fun updateProductQuantity(cartId: Long, productId: Long, newQuantity: Int): CartResponse {
        val cart = cartRepository.findByIdOrNull(cartId)
            ?: throw CartNotFoundException("Could not find cart with id: $cartId")

        val existingCartItem = cart.cartItems.find { it.product.id == productId }
            ?: throw CartItemNotFoundException("Product with id: $productId not found in cart: $cartId")

        if (newQuantity <= 0) {
            cart.cartItems.remove(existingCartItem) // Remove from the set, triggering deletion via orphanRemoval
        } else {
            existingCartItem.quantity = newQuantity
            existingCartItem.updateTotalPrice() // Update item's total price based on new quantity
        }
        cart.updateOverallTotalAmount() // Recalculate the cart's overall total
        val updatedCart = cartRepository.save(cart) // Save all changes
        return updatedCart.toCartResponse()
    }

    /**
     * Removes a specific product from a shopping cart.
     *
     * @param cartId The ID of the cart from which to remove the product.
     * @param productId The ID of the product to remove.
     * @return A [CartResponse] representing the updated cart.
     * @throws CartNotFoundException if the specified cart is not found.
     * @throws CartItemNotFoundException if the specified product is not found in the cart.
     */
    @Transactional
    override fun removeProductFromCart(cartId: Long, productId: Long): CartResponse {
        val cart = cartRepository.findByIdOrNull(cartId)
            ?: throw CartNotFoundException("Could not find cart with id: $cartId")

        val existingCartItem = cart.cartItems.find { it.product.id == productId }
            ?: throw CartItemNotFoundException("Product with id: $productId not found in cart: $cartId")

        cart.cartItems.remove(existingCartItem) // Remove from the set, triggering deletion via orphanRemoval
        cart.updateOverallTotalAmount() // Recalculate cart's overall total after removal
        val updatedCart = cartRepository.save(cart) // Save all changes
        return updatedCart.toCartResponse()
    }
}