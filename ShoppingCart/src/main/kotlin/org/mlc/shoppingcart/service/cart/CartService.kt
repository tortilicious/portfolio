package org.mlc.shoppingcart.service.cart

import org.mlc.shoppingcart.dto.cart.CartResponse
import java.math.BigDecimal

interface CartService {
    fun getCart(id: Long): CartResponse
    fun clearCart(id: Long)
    fun getTotalPrice(id: Long): BigDecimal
    fun createCart(): CartResponse
    fun addProduct(cartId: Long, productId: Long, quantity: Int): CartResponse
    fun updateProductQuantity(cartId: Long, productId: Long, newQuantity: Int): CartResponse
    fun removeProductFromCart(cartId: Long, productId: Long): CartResponse
}