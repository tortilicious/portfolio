package org.mlc.shoppingcart.dto.cart_item

import java.math.BigDecimal

data class CartItemResponse(
    var id: Long,
    val productName: String,
    var quantity: Int,
    var unitPrice: BigDecimal,
    var totalPrice: BigDecimal
)
