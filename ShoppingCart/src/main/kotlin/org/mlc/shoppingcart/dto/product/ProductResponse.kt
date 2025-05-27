package org.mlc.shoppingcart.dto.product

import java.math.BigDecimal

data class ProductResponse(
    val id: Long,
    val name: String,
    val brand: String,
    val description: String?,
    val price: BigDecimal,
    val inventory: Int,
    val categoryId: Long,
    val categoryName: String
)