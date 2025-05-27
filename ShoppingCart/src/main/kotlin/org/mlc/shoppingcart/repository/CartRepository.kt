package org.mlc.shoppingcart.repository

import org.mlc.shoppingcart.model.Cart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository interface for managing [Cart] entities.
 * Extends [JpaRepository] to provide standard CRUD (Create, Read, Update, Delete) operations for shopping carts,
 * with [Long] as the type of the ID.
 */
@Repository
interface CartRepository: JpaRepository<Cart, Long> {
}