// src/main/kotlin/org/mlc/shoppingcart/repository/CategoryRepository.kt
package org.mlc.shoppingcart.repository

import org.mlc.shoppingcart.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository interface for managing [Category] entities.
 * Extends [JpaRepository] to provide standard CRUD operations for categories,
 * with [Long] as the type of the ID.
 */
@Repository
interface CategoryRepository: JpaRepository<Category, Long> {
    /**
     * Retrieves a [Category] entity by its unique identifier.
     *
     * @param id The unique ID of the category to retrieve.
     * @return The [Category] entity if found, or `null` if no category with the given ID exists.
     */
    fun getCategoryById(id: Long): Category?

    /**
     * Finds a [Category] entity by its name.
     *
     * @param name The name of the category to find.
     * @return The [Category] entity if found, or `null` if no category with the given name exists.
     */
    fun findByName(name: String): Category?

    /**
     * Checks if a category with the specified name already exists in the database.
     *
     * @param name The name of the category to check for existence.
     * @return `true` if a category with the given name exists, `false` otherwise.
     */
    fun existsByName(name: String): Boolean
}