package org.mlc.shoppingcart.service.product

import CreateProductRequest
import org.mlc.shoppingcart.dto.product.ProductResponse
import org.mlc.shoppingcart.model.Product

/**
 * Interface for the product service layer.
 * Defines the business operations available for product management,
 * decoupling the business logic from persistence implementation details.
 */
interface ProductService {

    /**
     * Retrieves a single product by its unique identifier.
     *
     * @param id The unique ID of the product.
     * @return The [Product] with the given ID.
     * @throws NoSuchElementException if no product with the specified ID exists.
     */
    fun getProductById(id: Long): ProductResponse

    /**
     * Retrieves a list of products belonging to a specific category.
     *
     * @param categoryId The unique ID of the category.
     * @return A [List] of [Product] objects associated with the given category ID.
     * An empty list is returned if no products are found for the category.
     */
    fun getProductsByCategoryId(categoryId: Long): List<ProductResponse>

    /**
     * Retrieves a list of products by their brand name.
     *
     * @param brand The brand name of the products to retrieve.
     * @return A [List] of [Product] objects belonging to the specified brand.
     * An empty list is returned if no products are found for the brand.
     */
    fun getProductsByBrand(brand: String): List<ProductResponse>

    /**
     * Retrieves a list of products by both category ID and brand name.
     *
     * @param categoryId The unique ID of the category.
     * @param brand The brand name of the products.
     * @return A [List] of [Product] objects that match both the category ID and brand.
     * An empty list is returned if no products are found matching both criteria.
     */
    fun getProductsByCategoryIdAndBrand(categoryId: Long, brand: String): List<ProductResponse>

    /**
     * Retrieves a list of products by their name.
     *
     * @param name The name or partial name of the products to retrieve.
     * @return A [List] of [Product] objects whose name matches the given input.
     * An empty list is returned if no products are found with the specified name.
     */
    fun getProductsByName(name: String): List<ProductResponse>

    /**
     * Retrieves a list of products by both brand name and product name.
     *
     * @param brand The brand name of the products.
     * @param name The name or partial name of the products.
     * @return A [List] of [Product] objects that match both the brand and product name.
     * An empty list is returned if no products are found matching both criteria.
     */
    fun getProductsByBrandAndName(brand: String, name: String): List<ProductResponse>

    /**
     * Counts the number of products that match a given brand and product name.
     *
     * @param brand The brand name of the products to count.
     * @param name The name or partial name of the products to count.
     * @return The number of products that belong to the specified brand and have the given name.
     * Returns `0` if no products are found matching the criteria.
     */
    fun countProductsByBrandAndName(brand: String, name: String): Long

    /**
     * Saves a new product to the database.
     *
     * This method is intended only for creating *new* products.
     * If a product with the same ID already exists in the database, a [org.mlc.shoppingcart.error.PoductAlreadyExistsException] is thrown.
     *
     * @param product The [Product] object to be saved.
     * @return The newly saved [Product] entity, potentially with an updated ID from the database.
     * @throws org.mlc.shoppingcart.error.PoductAlreadyExistsException if a product with the same ID already exists.
     */
    fun saveProduct(productRequest: CreateProductRequest): ProductResponse

    /**
     * Deletes a product from the database by its unique identifier.
     *
     * @param id The ID of the product to delete.
     * @throws NoSuchElementException if no product with the specified ID exists.
     */
    fun deleteProduct(id: Long)

    /**
     * Updates the inventory quantity of a specific product.
     *
     * @param productId The ID of the product whose inventory is to be updated.
     * @param newInventory The new inventory quantity.
     * @return The updated [Product] entity.
     * @throws NoSuchElementException if no product with the specified ID exists.
     * @throws IllegalArgumentException if `newInventory` is negative.
     */
    fun updateProductInventory(productId: Long, newInventory: Int): Product

    /**
     * Searches for products based on a general search term that can match product name or description.
     *
     * @param searchTerm The term to search for.
     * @return A [List] of [Product] objects that match the search term.
     * Returns an empty list if no products are found matching the criteria.
     */
    fun searchProducts(searchTerm: String): List<ProductResponse>

}