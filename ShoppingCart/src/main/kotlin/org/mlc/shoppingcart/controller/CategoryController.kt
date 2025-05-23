package org.mlc.shoppingcart.controller

import org.mlc.shoppingcart.dto.category.CategoryResponse
import org.mlc.shoppingcart.dto.category.CreateCategoryRequest
import org.mlc.shoppingcart.dto.category.UpdateCategoryRequest
import org.mlc.shoppingcart.service.category.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for managing product categories.
 *
 * This controller handles HTTP requests related to category operations,
 * including retrieving all categories, getting a category by ID,
 * adding new categories, updating existing categories, and deleting categories.
 *
 * It acts as the entry point for API calls and delegates the actual business logic
 * to the [CategoryService].
 */
@RestController
@RequestMapping("/category")
class CategoryController(private val categoryService: CategoryService) {

    /**
     * Retrieves a list of all available categories.
     *
     * @return A [ResponseEntity] containing a list of [CategoryResponse] objects
     * and an HTTP status of OK (200).
     */
    @GetMapping("/all")
    fun getAllCategories(): ResponseEntity<List<CategoryResponse>> {
        val categories = categoryService.findAll()
        val response = ResponseEntity(categories, HttpStatus.OK)
        return response
    }

    /**
     * Retrieves a single category by its unique identifier.
     *
     * @param id The ID of the category to retrieve.
     * @return A [ResponseEntity] containing the [CategoryResponse] object for the
     * requested category and an HTTP status of OK (200).
     */
    @GetMapping("/{id}")
    fun getCategory(@PathVariable("id") id: Long): ResponseEntity<CategoryResponse> {
        val category = categoryService.findCategoryById(id)
        val response = ResponseEntity(category, HttpStatus.OK)
        return response
    }

    /**
     * Adds a new category to the system.
     *
     * The category details are provided in the request body.
     *
     * @param categoryRequest An object containing the details of the category to be created.
     * @return A [ResponseEntity] containing the [CategoryResponse] object of the
     * newly added category and an HTTP status of OK (200).
     */
    @PostMapping("/add")
    fun addCategory(
        @RequestBody categoryRequest: CreateCategoryRequest
    ): ResponseEntity<CategoryResponse> {
        val addedCategory = categoryService.createCategory(categoryRequest)
        val response = ResponseEntity<CategoryResponse>(addedCategory, HttpStatus.OK)
        return response
    }

    /**
     * Updates an existing category identified by its ID.
     *
     * The updated category details are provided in the request body.
     *
     * @param updateCategoryRequest An object containing the updated details for the category.
     * @param id The ID of the category to be updated.
     * @return A [ResponseEntity] containing the [CategoryResponse] object of the
     * updated category and an HTTP status of OK (200).
     */
    @PutMapping("/update/{id}")
    fun updateCategory(
        @RequestBody updateCategoryRequest: UpdateCategoryRequest,
        @PathVariable("id") id: Long
    ): ResponseEntity<CategoryResponse> {
        val updatedCategory = categoryService.updateCategory(id, updateCategoryRequest)
        val response = ResponseEntity<CategoryResponse>(updatedCategory, HttpStatus.OK)
        return response
    }

    /**
     * Deletes a category from the system by its unique identifier.
     *
     * @param id The ID of the category to be deleted.
     * @return A [ResponseEntity] with an HTTP status of OK (200) upon successful deletion.
     * The response body is empty ([Unit]) as there is no content to return.
     */
    @DeleteMapping("/delete/{id}")
    fun deleteCategory(@PathVariable("id") id: Long): ResponseEntity<Unit> {
        categoryService.deleteCategory(id)
        return ResponseEntity(HttpStatus.OK)
    }
}