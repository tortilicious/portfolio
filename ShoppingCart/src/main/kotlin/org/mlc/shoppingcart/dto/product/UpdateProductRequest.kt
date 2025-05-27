package org.mlc.shoppingcart.dto.product

import java.math.BigDecimal

data class UpdateProductRequest(
    val name: String?,
    val brand: String?,
    val description: String?,
    val price: BigDecimal?,
    val inventory: Int?,
    val categoryName: String?,
)