package org.mlc.shoppingcart.mapper

import org.mlc.shoppingcart.dto.cart.CartResponse
import org.mlc.shoppingcart.dto.cart_item.CartItemResponse
import org.mlc.shoppingcart.dto.category.CategoryResponse
import org.mlc.shoppingcart.dto.image.ImageResponse
import org.mlc.shoppingcart.dto.product.ProductResponse
import org.mlc.shoppingcart.model.*

fun Product.toProductResponse(): ProductResponse {
    return ProductResponse(
        id = id,
        name = name,
        brand = brand,
        description = description ?: "No description available",
        price = price,
        inventory = inventory,
        categoryId = category.id,
        categoryName = category.name
    )
}


fun Category.toCategoryResponse(): CategoryResponse {
    return CategoryResponse(
        id = id,
        name = name
    )
}

fun Image.toImageResponse(): ImageResponse {
    return ImageResponse(
        id = id,
        fileName = fileName,
        fileType = fileType,
        downloadUrl = downloadUrl,
        productId = product.id
    )
}

fun Cart.toCartResponse(): CartResponse {
    return CartResponse(
        id = id,
        totalAmount = totalAmount,
        cartItems = cartItems.map { it.toCartItemResponse() }
    )
}

fun CartItem.toCartItemResponse(): CartItemResponse {
    return CartItemResponse(
        id = id,
        productName = product.name,
        quantity = quantity,
        unitPrice = unitPrice,
        totalPrice = totalPrice
    )
}