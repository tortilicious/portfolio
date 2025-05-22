package org.mlc.shoppingcart.service.product

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
     * @return The [Product] found, or `null` if no such product exists.
     */
    fun getProductById(id: Long): Product?

    /**
     * Retrieves a list of products belonging to a specific category.
     *
     * @param categoryId The unique ID of the category.
     * @return A [List] of [Product] objects associated with the given category ID,
     * or an empty list if no products are found.
     */
    fun getProductsByCategoryId(categoryId: Long): List<Product>

    /**
     * Retrieves a list of products by their brand name.
     *
     * @param brand The brand name of the products to retrieve.
     * @return A [List] of [Product] objects belonging to the specified brand,
     * or an empty list if no products are found.
     */
    fun getProductsByBrand(brand: String): List<Product>

    /**
     * Retrieves a list of products by both category ID and brand name.
     *
     * @param categoryId The unique ID of the category.
     * @param brand The brand name of the products.
     * @return A [List] of [Product] objects that match both the category ID and brand,
     * or an empty list if no products are found.
     */
    fun getProductsByCategoryIdAndBrand(categoryId: Long, brand: String): List<Product>

    /**
     * Retrieves a list of products by their name.
     *
     * @param name The name or partial name of the products to retrieve.
     * @return A [List] of [Product] objects whose name matches the given input,
     * or an empty list if no products are found.
     */
    fun getProductsByName(name: String): List<Product>

    /**
     * Retrieves a list of products by both brand name and product name.
     *
     * @param brand The brand name of the products.
     * @param name The name or partial name of the products.
     * @return A [List] of [Product] objects that match both the brand and product name,
     * or an empty list if no products are found.
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

    /**
     * Saves a new product or updates an existing one.
     *
     * @param product The [Product] to be saved or updated.
     * @return The saved [Product] (with its generated ID if new).
     */
    fun saveProduct(product: Product): Product

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id The ID of the product to delete.
     */
    fun deleteProduct(id: Long)

    /**
     * Updates the inventory level for a specific product.
     *
     * @param productId The ID of the product whose inventory is to be updated.
     * @param newInventory The new inventory quantity.
     * @return The updated [Product], or `null` if the product is not found.
     */
    fun updateProductInventory(productId: Long, newInventory: Int): Product?

}