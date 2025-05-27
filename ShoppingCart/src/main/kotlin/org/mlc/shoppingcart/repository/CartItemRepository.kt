package org.mlc.shoppingcart.repository

import org.mlc.shoppingcart.model.CartItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository interface for managing [CartItem] entities.
 * Extends [JpaRepository] to provide standard CRUD (Create, Read, Update, Delete) operations for cart items,
 * with [Long] as the type of the ID.
 */
@Repository
interface CartItemRepository: JpaRepository<CartItem, Long> {
}