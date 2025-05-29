package org.mlc.shoppingcart.controller

import jakarta.validation.Valid
import org.mlc.shoppingcart.dto.cart.CartResponse
import org.mlc.shoppingcart.dto.cart_item.AddItemToCartRequest
import org.mlc.shoppingcart.dto.cart_item.UpdateCartItemRequest
import org.mlc.shoppingcart.service.cart.CartService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

/**
 * REST Controller for managing shopping cart operations.
 * Handles HTTP requests related to creating, retrieving, updating, and deleting carts and their items.
 */
@RestController
@RequestMapping("/cart")
class CartController(
    private val cartService: CartService

) {


    /**
     * Retrieves a shopping cart by its unique ID.
     *
     * @param id The unique identifier of the cart to retrieve from the path.
     * @return A [ResponseEntity] containing the [CartResponse] for the retrieved cart
     * and HTTP status 200 (OK).
     */
    @GetMapping("/{id}/my-cart") // Handles GET requests to /cart/{id}/my-cart
    fun getCart(@PathVariable("id") id: Long): ResponseEntity<CartResponse> {
        val cart = cartService.getCart(id)
        return ResponseEntity(cart, HttpStatus.OK)
    }

    /**
     * Retrieves the total price of a shopping cart by its unique ID.
     *
     * @param id The unique identifier of the cart from the path.
     * @return A [ResponseEntity] containing the [BigDecimal] total price
     * and HTTP status 200 (OK).
     */
    @GetMapping("/{id}/my-cart/total-price")
    fun getCartTotalPrice(@PathVariable("id") id: Long): ResponseEntity<BigDecimal> {
        val totalPrice = cartService.getTotalPrice(id)
        return ResponseEntity(totalPrice, HttpStatus.OK)
    }

    /**
     * Adds a product to the specified shopping cart or updates its quantity if it already exists.
     *
     * @param cartId The unique identifier of the cart to add the product to, from the path.
     * @param request The [AddItemToCartRequest] containing the product ID and quantity from the request body.
     * @return A [ResponseEntity] containing the updated [CartResponse]
     * and HTTP status 200 (OK).
     */
    @PostMapping("/{cartId}/items")
    fun addCartItem(
        @PathVariable("cartId") cartId: Long,
        @Valid @RequestBody request: AddItemToCartRequest
    ): ResponseEntity<CartResponse> {
        val cartResponse = cartService.addProduct(
            cartId = cartId,
            productId = request.productId,
            quantity = request.quantity,
        )
        return ResponseEntity(cartResponse, HttpStatus.OK)
    }

    /**
     * Updates the quantity of a specific product within a shopping cart.
     * If the new quantity is zero or less, the item will be removed from the cart.
     *
     * @param cartId The unique identifier of the cart containing the item, from the path.
     * @param productId The unique identifier of the product whose quantity needs to be updated, from the path.
     * @param request The [UpdateCartItemRequest] containing the new quantity from the request body.
     * @return A [ResponseEntity] containing the updated [CartResponse]
     * and HTTP status 200 (OK).
     */
    @PutMapping("/{cartId}/items/{productId}")
    fun updateCartItem(
        @PathVariable("cartId") cartId: Long,
        @PathVariable("productId") productId: Long,
        @Valid @RequestBody request: UpdateCartItemRequest
    ): ResponseEntity<CartResponse> {
        val updatedCart = cartService.updateProductQuantity(
            cartId = cartId,
            productId = productId,
            newQuantity = request.quantity
        )
        return ResponseEntity(updatedCart, HttpStatus.OK)
    }

    /**
     * Clears all items from a specified shopping cart.
     *
     * @param id The unique identifier of the cart to clear, from the path.
     * @return A [ResponseEntity] with HTTP status 200 (OK) on successful clearance.
     */
    @DeleteMapping("/{id}/my-cart/clear")
    fun clearCart(@PathVariable("id") id: Long): ResponseEntity<Unit> {
        cartService.clearCart(id)
        return ResponseEntity(HttpStatus.OK)
    }

    /**
     * Removes a specific product from a shopping cart.
     *
     * @param cartId The unique identifier of the cart from which to remove the product, from the path.
     * @param productId The unique identifier of the product to remove, from the path.
     * @return A [ResponseEntity] containing the updated [CartResponse]
     * and HTTP status 200 (OK).
     */
    @DeleteMapping("/{cartId}/items/{productId}")
    fun removeCartItem(
        @PathVariable("cartId") cartId: Long,
        @PathVariable("productId") productId: Long,
    ): ResponseEntity<CartResponse> {
        val updatedCart = cartService.removeProductFromCart(
            cartId = cartId,
            productId = productId,
        )
        return ResponseEntity(updatedCart, HttpStatus.OK)
    }
}