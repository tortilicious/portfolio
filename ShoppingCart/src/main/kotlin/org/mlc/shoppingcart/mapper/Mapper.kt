package org.mlc.shoppingcart.mapper

import org.mlc.shoppingcart.dto.user.UserResponse
import org.mlc.shoppingcart.dto.cart.CartResponse
import org.mlc.shoppingcart.dto.cart_item.CartItemResponse
import org.mlc.shoppingcart.dto.category.CategoryResponse
import org.mlc.shoppingcart.dto.image.ImageResponse
import org.mlc.shoppingcart.dto.order.OrderResponse
import org.mlc.shoppingcart.dto.order_item.OrderItemResponse
import org.mlc.shoppingcart.dto.product.ProductResponse
import org.mlc.shoppingcart.model.*

fun Product.toProductResponse(): ProductResponse {
    return ProductResponse(
        id = id,
        name = name,
        brand = brand,
        description = description ?: "No description available",
        price = price,
        inventory = stock,
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

fun User.toUserResponse(): UserResponse {
    return UserResponse(
        id = id,
        email = email
    )
}

fun OrderItem.toOrderItemResponse(): OrderItemResponse {
    return OrderItemResponse(
        id = id,
        productId = product.id,
        productName = product.name,
        productQuantity = quantity,
        totalPrice = totalPrice,
    )
}

fun Order.toOrderResponse(): OrderResponse {
    return OrderResponse(
        id = id,
        status = status,
        email = user.email,
        firstName = user.firstName,
        lastName = user.lastName,
        date = date,
        totalAmount = totalAmount,
        items = orderItems.map { it.toOrderItemResponse() }.toMutableSet()
    )
}