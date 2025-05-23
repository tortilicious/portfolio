package org.mlc.shoppingcart.service.product

import CreateProductRequest
import org.mlc.shoppingcart.dto.product.ProductResponse
import org.mlc.shoppingcart.dto.product.UpdateProductRequest
import org.mlc.shoppingcart.error.ProductAlreadyExistsException
import org.mlc.shoppingcart.error.ProductNotFoundException
import org.mlc.shoppingcart.mapper.toProductResponse
import org.mlc.shoppingcart.model.Category
import org.mlc.shoppingcart.model.Product
import org.mlc.shoppingcart.repository.CategoryRepository
import org.mlc.shoppingcart.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service implementation for managing [Product] entities.
 *
 * This class provides the business logic operations for products, acting as an intermediary
 * between the presentation layer (e.g., controllers) and the data access layer ([ProductRepository]).
 * It handles specific business rules, validations, and error handling for product-related operations.
 */
@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ProductService {

    /**
     * Retrieves a single product by its unique identifier.
     *
     * @param id The unique ID of the product to retrieve.
     * @return The [ProductResponse] representing the found product.
     * @throws ProductNotFoundException if no product with the specified ID exists.
     */
    override fun getProductById(id: Long): ProductResponse {
        val product = productRepository.getProductById(id)
            ?: throw ProductNotFoundException("Product with id '$id' does not exist.")
        return product.toProductResponse()
    }

    /**
     * Retrieves a list of products belonging to a specific category.
     *
     * @param categoryId The unique ID of the category.
     * @return A [List] of [ProductResponse] objects associated with the given category ID.
     * Returns an empty list if no products are found for the category.
     */
    override fun getProductsByCategoryId(categoryId: Long): List<ProductResponse> {
        return productRepository.getProductsByCategoryId(categoryId).map { it.toProductResponse() }
    }

    /**
     * Retrieves a list of products by their brand name.
     *
     * @param brand The brand name of the products to retrieve.
     * @return A [List] of [ProductResponse] objects belonging to the specified brand.
     * Returns an empty list if no products are found for the brand.
     */
    override fun getProductsByBrand(brand: String): List<ProductResponse> {
        return productRepository.getProductsByBrand(brand).map { it.toProductResponse() }
    }

    /**
     * Retrieves a list of products by both category ID and brand name.
     *
     * @param categoryId The unique ID of the category.
     * @param brand The brand name of the products.
     * @return A [List] of [ProductResponse] objects that match both the category ID and brand.
     * Returns an empty list if no products are found matching both criteria.
     */
    override fun getProductsByCategoryIdAndBrand(categoryId: Long, brand: String): List<ProductResponse> {
        return productRepository.getProductsByCategoryIdAndBrand(categoryId, brand).map { it.toProductResponse() }
    }

    /**
     * Retrieves a list of products by their name.
     * The search is performed case-insensitively.
     *
     * @param name The name or partial name of the products to retrieve.
     * @return A [List] of [ProductResponse] objects whose name matches the given input.
     * Returns an empty list if no products are found with the specified name.
     */
    override fun getProductsByName(name: String): List<ProductResponse> {
        return productRepository.getProductsByName(name.trim().lowercase()).map { it.toProductResponse() }
    }

    /**
     * Retrieves a list of products by both brand name and product name.
     * The search is performed case-insensitively for the product name.
     *
     * @param brand The brand name of the products.
     * @param name The name or partial name of the products.
     * @return A [List] of [ProductResponse] objects that match both the brand and product name.
     * Returns an empty list if no products are found matching both criteria.
     */
    override fun getProductsByBrandAndName(brand: String, name: String): List<ProductResponse> {
        return productRepository.getProductsByBrandAndName(brand, name.trim().lowercase())
            .map { it.toProductResponse() }
    }

    /**
     * Counts the number of products that match a given brand and product name.
     *
     * @param brand The brand name of the products to count.
     * @param name The name or partial name of the products to count.
     * @return The number of products that belong to the specified brand and have the given name.
     * Returns `0` if no products are found matching the criteria.
     */
    override fun countProductsByBrandAndName(brand: String, name: String): Long {
        val listOfProducts = productRepository.getProductsByBrandAndName(brand, name)
        return listOfProducts.count().toLong()
    }

    /**
     * Creates a new product in the database.
     *
     * This method first checks for duplicate products based on name and brand.
     * If the specified category does not exist, it creates the category on the fly.
     *
     * @param productRequest The [CreateProductRequest] containing the details for the new product.
     * @return The newly created [ProductResponse] with its generated ID and associated category.
     * @throws ProductAlreadyExistsException if a product with the same name and brand already exists.
     */
    @Transactional
    override fun saveProduct(productRequest: CreateProductRequest): ProductResponse {
        val normalizedProductName = productRequest.name.trim().lowercase()
        val normalizedBrand = productRequest.brand.trim().lowercase()

        val existingProducts = productRepository.getProductsByBrandAndName(normalizedBrand, normalizedProductName)
        if (existingProducts.isNotEmpty()) {
            throw ProductAlreadyExistsException("Product with name '${productRequest.name}' and brand '${productRequest.brand}' already exists.")
        }

        val normalizedCategoryName = productRequest.categoryName.trim().lowercase()
        val category = categoryRepository.findByName(normalizedCategoryName)
            ?: run {
                val newCategory = Category(name = productRequest.categoryName.trim().lowercase())
                categoryRepository.save(newCategory)
            }


        val newProduct = Product(
            name = productRequest.name.trim(),
            brand = productRequest.brand.trim(),
            description = productRequest.description?.trim(),
            price = productRequest.price,
            inventory = productRequest.inventory,
            images = mutableListOf(),
            category = category
        )

        val savedProduct = productRepository.save(newProduct)
        return savedProduct.toProductResponse()
    }

    /**
     * Deletes a product from the database by its unique identifier.
     *
     * @param id The ID of the product to delete.
     * @throws ProductNotFoundException if no product with the specified ID exists.
     */
    @Transactional
    override fun deleteProduct(id: Long) {
        val product = productRepository.getProductById(id)
            ?: throw ProductNotFoundException("Product with id '$id' does not exist and cannot be deleted.")
        productRepository.delete(product)
    }

    /**
     * Updates an existing product with the fields provided in the request.
     * Only non-null fields in the [UpdateProductRequest] will be applied to the entity.
     * The category will be searched by name and created if it doesn't exist.
     *
     * @param id The ID of the product to update.
     * @param updateRequest The [UpdateProductRequest] with the fields to modify.
     * @return The [ProductResponse] of the updated product.
     * @throws ProductNotFoundException if the product does not exist.
     * @throws IllegalArgumentException if any invalid field is provided (e.g., negative inventory).
     */
    @Transactional
    override fun updateProduct(id: Long, updateRequest: UpdateProductRequest): ProductResponse {

        val productToUpdate = productRepository.getProductById(id)
            ?: throw ProductNotFoundException("Product with id '$id' does not exist.")

        updateRequest.name?.let { productToUpdate.name = it.trim() }
        updateRequest.brand?.let { productToUpdate.brand = it.trim() }
        updateRequest.description?.let { productToUpdate.description = it.trim() }
        updateRequest.price?.let { productToUpdate.price = it }

        updateRequest.inventory?.let { newInventory ->
            if (newInventory < 0) {
                throw IllegalArgumentException("Inventory cannot be negative.")
            }
            productToUpdate.inventory = newInventory
        }

        updateRequest.categoryName?.let { categoryNameFromRequest ->
            val normalizedCategoryName = categoryNameFromRequest.trim().lowercase()

            val categoryToAssign = categoryRepository.findByName(normalizedCategoryName)
                ?: run {
                    val newCategory = Category(name = categoryNameFromRequest.trim())
                    categoryRepository.save(newCategory)
                }

            productToUpdate.category = categoryToAssign
        }

        val updatedProduct = productRepository.save(productToUpdate)
        return updatedProduct.toProductResponse()
    }


    /**
     * Searches for products based on a general search term that can match product name or description.
     * This method is a placeholder and requires specific implementation based on search requirements.
     *
     * @param searchTerm The term to search for.
     * @return A [List] of [ProductResponse] objects that match the search term.
     * Returns an empty list if no products are found matching the criteria.
     */
    override fun getAllProducts(): List<ProductResponse> {
        return productRepository.findAll().map { it.toProductResponse() }
    }
}