package org.mlc.shoppingcart.service.category

import org.mlc.shoppingcart.dto.category.CategoryResponse
import org.mlc.shoppingcart.dto.category.CreateCategoryRequest
import org.mlc.shoppingcart.dto.category.UpdateCategoryRequest
import org.mlc.shoppingcart.error.CategoryAlreadyExistsException

import org.mlc.shoppingcart.error.CategoryNotFoundException
import org.mlc.shoppingcart.mapper.toCategoryResponse
import org.mlc.shoppingcart.model.Category
import org.mlc.shoppingcart.repository.CategoryRepository
import org.mlc.shoppingcart.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service implementation for managing [Category] entities.
 *
 * This class provides the business logic operations for categories, acting as an intermediary
 * between the presentation layer (e.g., controllers) and the data access layer ([CategoryRepository]).
 * It handles specific business rules, validations, and error handling for category-related operations.
 */
@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) : CategoryService {

    /**
     * Retrieves a category by its unique identifier.
     *
     * @param id The unique ID of the category.
     * @return The [CategoryResponse] representing the found category.
     * @throws CategoryNotFoundException if no category with the specified ID exists.
     */
    override fun findCategoryById(id: Long): CategoryResponse {
        return categoryRepository.findByIdOrNull(id)
            ?.toCategoryResponse()
            ?: throw CategoryNotFoundException("Category with id: '$id' not found.")
    }

    /**
     * Retrieves a category by its name.
     *
     * @param name The name of the category.
     * @return The [CategoryResponse] representing the found category.
     * @throws CategoryNotFoundException if no category with the specified name exists.
     */
    override fun findCategoryByName(name: String): CategoryResponse {
        val normalizedName = name.trim().lowercase()
        return categoryRepository.findByName(normalizedName)
            ?.toCategoryResponse()
            ?: throw CategoryNotFoundException("Category with name: '$name' not found.")
    }

    /**
     * Retrieves a list of all categories.
     *
     * @return A [List] of [CategoryResponse] objects representing all categories.
     * Returns an empty list if no categories are found.
     */
    override fun findAll(): List<CategoryResponse> {
        return categoryRepository.findAll().map { it.toCategoryResponse() }
    }

    /**
     * Creates a new category in the database.
     *
     * @param createRequest The [CreateCategoryRequest] containing the details for the new category.
     * @return The newly created [CategoryResponse] with its generated ID.
     * @throws CategoryAlreadyExistsException if a category with the same name already exists.
     */
    @Transactional
    override fun createCategory(createRequest: CreateCategoryRequest): CategoryResponse {
        val normalizedName = createRequest.name.trim().lowercase()

        if (categoryRepository.existsByName(normalizedName)) {
            throw CategoryAlreadyExistsException("Category with name: '${createRequest.name}' already exists.")
        }

        val category = Category(
            name = createRequest.name.trim()
        )

        val savedCategory = categoryRepository.save(category)
        return savedCategory.toCategoryResponse()
    }

    /**
     * Updates an existing category in the database.
     *
     * @param id The ID of the category to update.
     * @param updateRequest The [UpdateCategoryRequest] containing the updated details.
     * @return The updated [CategoryResponse].
     * @throws CategoryNotFoundException if no category with the specified ID exists.
     * @throws CategoryAlreadyExistsException if the new name conflicts with an existing category.
     */
    @Transactional
    override fun updateCategory(id: Long, updateRequest: UpdateCategoryRequest): CategoryResponse {
        val categoryToUpdate = categoryRepository.findByIdOrNull(id)
            ?: throw CategoryNotFoundException("Category with id: '$id' not found for update.")

        val normalizedName = updateRequest.name.trim().lowercase()

        val categoryWithSameName = categoryRepository.findByName(normalizedName)
        if (categoryWithSameName != null && categoryWithSameName.id != id) {
            throw CategoryAlreadyExistsException("Another category with name '${updateRequest.name}' already exists.")
        }

        categoryToUpdate.name = updateRequest.name.trim()

        val updatedCategory = categoryRepository.save(categoryToUpdate)
        return updatedCategory.toCategoryResponse()
    }

    /**
     * Deletes a category from the database by its unique identifier.
     *
     * @param id The ID of the category to delete.
     * @throws CategoryNotFoundException if no category with the specified ID exists.
     * @throws IllegalStateException if the category has associated products.
     */
    @Transactional
    override fun deleteCategory(id: Long) {
        val categoryToDelete = categoryRepository.findByIdOrNull(id)
            ?: throw CategoryNotFoundException("Category with id: '$id' not found and cannot be deleted.")

        if (productRepository.countProductsByCategoryId(id) > 0) {
            throw IllegalStateException("Category with id: '$id' cannot be deleted as it has associated products.")
        }

        categoryRepository.delete(categoryToDelete)
    }

    /**
     * Checks if a category with the given ID exists.
     *
     * @param id The unique ID of the category.
     * @return `true` if a category with the ID exists, `false` in case of otherwise.
     */
    override fun existsById(id: Long): Boolean {
        return categoryRepository.existsById(id)
    }

    /**
     * Checks if a category with the given name exists (case-insensitive).
     *
     * @param name The name of the category.
     * @return `true` if a category with the name exists, `false` otherwise.
     */
    override fun existsByName(name: String): Boolean {
        return categoryRepository.existsByName(name.trim().lowercase())
    }
}