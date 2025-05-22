package org.mlc.shoppingcart.service.product

import org.mlc.shoppingcart.error.PoductAlreadyExistsException
import org.mlc.shoppingcart.model.Product
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
@Service // Marks this class as a Spring Service component
class ProductServiceImpl(private val productRepository: ProductRepository) : ProductService {

    /**
     * Retrieves a single product by its unique identifier.
     *
     * @param id The unique ID of the product to retrieve.
     * @return The [Product] with the given ID.
     * @throws NoSuchElementException if no product with the specified ID exists.
     */
    override fun getProductById(id: Long): Product {
        return productRepository.getProductById(id)
            ?: throw NoSuchElementException("Product with id '$id' does not exist.")
    }

    /**
     * Retrieves a list of products belonging to a specific category.
     *
     * @param categoryId The unique ID of the category.
     * @return A [List] of [Product] objects associated with the given category ID.
     * An empty list is returned if no products are found for the category.
     */
    override fun getProductsByCategoryId(categoryId: Long): List<Product> {
        return productRepository.getProductsByCategoryId(categoryId)
    }

    /**
     * Retrieves a list of products by their brand name.
     *
     * @param brand The brand name of the products to retrieve.
     * @return A [List] of [Product] objects belonging to the specified brand.
     * An empty list is returned if no products are found for the brand.
     */
    override fun getProductsByBrand(brand: String): List<Product> {
        return productRepository.getProductsByBrand(brand)
    }

    /**
     * Retrieves a list of products by both category ID and brand name.
     *
     * @param categoryId The unique ID of the category.
     * @param brand The brand name of the products.
     * @return A [List] of [Product] objects that match both the category ID and brand.
     * An empty list is returned if no products are found matching both criteria.
     */
    override fun getProductsByCategoryIdAndBrand(categoryId: Long, brand: String): List<Product> {
        return productRepository.getProductsByCategoryIdAndBrand(categoryId, brand)
    }

    /**
     * Retrieves a list of products by their name.
     *
     * @param name The name or partial name of the products to retrieve.
     * @return A [List] of [Product] objects whose name matches the given input.
     * An empty list is returned if no products are found with the specified name.
     */
    override fun getProductsByName(name: String): List<Product> {
        return productRepository.getProductsByName(name)
    }

    /**
     * Retrieves a list of products by both brand name and product name.
     *
     * @param brand The brand name of the products.
     * @param name The name or partial name of the products.
     * @return A [List] of [Product] objects that match both the brand and product name.
     * An empty list is returned if no products are found matching both criteria.
     */
    override fun getProductsByBrandAndName(brand: String, name: String): List<Product> {
        return productRepository.getProductsByBrandAndName(brand, name)
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
     * Saves a new product to the database.
     *
     * This method is intended only for creating *new* products.
     * If a product with the same ID already exists in the database, a [PoductAlreadyExistsException] is thrown.
     *
     * @param product The [Product] object to be saved.
     * @return The newly saved [Product] entity, potentially with an updated ID from the database.
     * @throws PoductAlreadyExistsException if a product with the same ID already exists.
     */
    @Transactional
    override fun saveProduct(product: Product): Product {
        return productRepository.getProductById(product.id)
            ?.let { throw PoductAlreadyExistsException("The product with ID '${product.id}' already exists in the database. Use an update method to modify it.") }
            ?: productRepository.save(product)
    }

    /**
     * Deletes a product from the database by its unique identifier.
     *
     * @param id The ID of the product to delete.
     * @throws NoSuchElementException if no product with the specified ID exists.
     */
    @Transactional
    override fun deleteProduct(id: Long) {
        // Check if the product exists before attempting to delete to provide a clear error message.
        val product = productRepository.getProductById(id)
            ?: throw NoSuchElementException("Product with id '$id' does not exist and cannot be deleted.")
        productRepository.delete(product)
    }

    /**
     * Updates the inventory quantity of a specific product.
     *
     * @param productId The ID of the product whose inventory is to be updated.
     * @param newInventory The new inventory quantity.
     * @return The updated [Product] entity.
     * @throws NoSuchElementException if no product with the specified ID exists.
     * @throws IllegalArgumentException if `newInventory` is negative.
     */
    @Transactional
    override fun updateProductInventory(productId: Long, newInventory: Int): Product {
        // Fetch the existing product. Throw if not found.
        val product = productRepository.getProductById(productId)
            ?: throw NoSuchElementException("Product with id '$productId' does not exist for inventory update.")

        // Add business validation for newInventory
        if (newInventory < 0) {
            throw IllegalArgumentException("Inventory cannot be negative. Provided: $newInventory")
        }

        // Directly modify the 'inventory' property as it's now a 'var'
        product.inventory = newInventory

        // Save the updated product. Spring Data JPA's save() will perform an update because the ID exists.
        return productRepository.save(product)
    }

}