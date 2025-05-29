package org.mlc.shoppingcart.repository

import org.mlc.shoppingcart.model.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository: JpaRepository<Order, Long> {
}