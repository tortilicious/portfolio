package org.mlc.shoppingcart.service.category

import org.mlc.shoppingcart.dto.category.CategoryResponse
import org.mlc.shoppingcart.dto.category.CreateCategoryRequest
import org.mlc.shoppingcart.dto.category.UpdateCategoryRequest

/**
 * Interface for the category service layer.
 * Defines the business operations available for managing product categories.
 */
interface CategoryService {

    /**
     * Retrieves a category by its unique identifier.
     *
     * @param id The unique ID of the category.
     * @return The [CategoryResponse] representing the found category.
     * @throws org.mlc.shoppingcart.error.CategoryNotFoundException if no category with the specified ID exists.
     */
    fun findCategoryById(id: Long): CategoryResponse

    /**
     * Retrieves a category by its name.
     *
     * @param name The name of the category.
     * @return The [CategoryResponse] representing the found category.
     * @throws org.mlc.shoppingcart.error.CategoryNotFoundException if no category with the specified name exists.
     */
    fun findCategoryByName(name: String): CategoryResponse

    /**
     * Retrieves a list of all categories.
     *
     * @return A [List] of [CategoryResponse] objects representing all categories.
     * Returns an empty list if no categories are found.
     */
    fun findAll(): List<CategoryResponse>

    /**
     * Creates a new category in the database.
     *
     * @param createRequest The [CreateCategoryRequest] containing the details for the new category.
     * @return The newly created [CategoryResponse] with its generated ID.
     * @throws org.mlc.shoppingcart.error.CategoryAlreadyExistsException if a category with the same name already exists.
     */
    fun createCategory(createRequest: CreateCategoryRequest): CategoryResponse

    /**
     * Updates an existing category in the database.
     *
     * @param id The ID of the category to update.
     * @param updateRequest The [UpdateCategoryRequest] containing the updated details.
     * @return The updated [CategoryResponse].
     * @throws org.mlc.shoppingcart.error.CategoryNotFoundException if no category with the specified ID exists.
     * @throws org.mlc.shoppingcart.error.CategoryAlreadyExistsException if the new name conflicts with an existing category.
     */
    fun updateCategory(id: Long, updateRequest: UpdateCategoryRequest): CategoryResponse

    /**
     * Deletes a category from the database by its unique identifier.
     *
     * @param id The ID of the category to delete.
     * @throws org.mlc.shoppingcart.error.CategoryNotFoundException if no category with the specified ID exists.
     * @throws IllegalStateException if the category has associated products.
     */
    fun deleteCategory(id: Long): Unit

    /**
     * Checks if a category with the given ID exists.
     *
     * @param id The unique ID of the category.
     * @return `true` if a category with the ID exists, `false` otherwise.
     */
    fun existsById(id: Long): Boolean

    /**
     * Checks if a category with the given name exists (case-insensitive).
     *
     * @param name The name of the category.
     * @return `true` if a category with the name exists, `false` otherwise.
     */
    fun existsByName(name: String): Boolean
}