// src/main/kotlin/org/mlc/shoppingcart/repository/CategoryRepository.kt
package org.mlc.shoppingcart.repository

import org.mlc.shoppingcart.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository // Add @Repository import

@Repository
interface CategoryRepository: JpaRepository<Category, Long> {
    fun findByIdOrNull(id: Long): Category?
    fun findByName(name: String): Category?
    fun existsByName(name: String): Boolean
}