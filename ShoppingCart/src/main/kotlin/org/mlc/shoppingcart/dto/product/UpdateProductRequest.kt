package org.mlc.shoppingcart.dto.product

import org.mlc.shoppingcart.model.Image

data class UpdateProductRequest(
    val description: String?,
    val price: Double?,
    val inventory: Int?,
    val images: List<Image>?,
    val categoryId: Long?,
)