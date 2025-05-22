package org.mlc.shoppingcart.repository

import org.mlc.shoppingcart.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository interface for managing [Product] entities.
 *
 * This interface extends [JpaRepository] to provide standard CRUD operations
 * for products and defines custom query methods to retrieve products based on
 * various criteria such as ID, category, and brand.
 */
@Repository
interface ProductRepository: JpaRepository<Product, Long> {

    /**
     * Retrieves a single product by its unique identifier.
     *
     * @param id The unique ID of the product to retrieve.
     * @return The [Product] with the given ID, or `null` if no such product exists.
     */
    fun getProductById(id: Long): Product?

    /**
     * Retrieves a list of products belonging to a specific category.
     *
     * @param categoryId The unique ID of the category.
     * @return A [List] of [Product] objects associated with the given category ID.
     * Returns an empty list if no products are found for the category.
     */
    fun getProductsByCategoryId(categoryId: Long): List<Product>

    /**
     * Retrieves a list of products by their brand name.
     *
     * @param brand The brand name of the products to retrieve.
     * @return A [List] of [Product] objects belonging to the specified brand.
     * Returns an empty list if no products are found for the brand.
     */
    fun getProductsByBrand(brand: String): List<Product>

    /**
     * Retrieves a list of products by both category ID and brand name.
     *
     * @param categoryId The unique ID of the category.
     * @param brand The brand name of the products.
     * @return A [List] of [Product] objects that match both the category ID and brand.
     * Returns an empty list if no products are found matching both criteria.
     */
    fun getProductsByCategoryIdAndBrand(categoryId: Long, brand: String): List<Product>

    /**
     * Retrieves a list of products by their name.
     *
     * @param name The name or partial name of the products to retrieve.
     * @return A [List] of [Product] objects whose name matches the given input.
     * Returns an empty list if no products are found with the specified name.
     */
    fun getProductsByName(name: String): List<Product>

    /**
     * Retrieves a list of products by both brand name and product name.
     *
     * @param brand The brand name of the products.
     * @param name The name or partial name of the products.
     * @return A [List] of [Product] objects that match both the brand and product name.
     * Returns an empty list if no products are found matching both criteria.
     */
    fun getProductsByBrandAndName(brand: String, name: String): List<Product>

    /**
     * Counts the number of products that match a given brand and product name.
     *
     * @param brand The brand name of the products to count.
     * @param name The name or partial name of the products to count.
     * @return The number of products that belong to the specified brand and have the given name.
     */
    fun countProductsByBrandAndName(brand: String, name: String): Long

    fun countProductsByCategoryId(categoryId: Long): Long
}