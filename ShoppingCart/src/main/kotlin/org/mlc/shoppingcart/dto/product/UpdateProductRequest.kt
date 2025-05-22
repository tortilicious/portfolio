package org.mlc.shoppingcart.dto.product

data class UpdateProductRequest(
    val name: String?,
    val brand: String?,
    val description: String?,
    val price: Double?,
    val inventory: Int?,
    val categoryName: String?,
)