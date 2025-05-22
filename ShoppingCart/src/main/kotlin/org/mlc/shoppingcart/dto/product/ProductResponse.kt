package org.mlc.shoppingcart.dto.product

data class ProductResponse(
    val id: Long,
    val name: String,
    val brand: String,
    val description: String?,
    val price: Double,
    val inventory: Int,
    val categoryId: Long,
    val categoryName: String
)