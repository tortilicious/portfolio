package org.mlc.shoppingcart.controller

import CreateProductRequest
import org.mlc.shoppingcart.dto.product.ProductResponse
import org.mlc.shoppingcart.dto.product.UpdateProductRequest
import org.mlc.shoppingcart.service.product.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for managing product-related operations.
 * This controller handles HTTP requests for retrieving, adding, updating, and deleting products.
 *
 * @param productService The service layer responsible for product business logic.
 */
@RestController
@RequestMapping("/product")
class ProductController(private val productService: ProductService) {

    /**
     * Retrieves all products available in the system.
     *
     * @return A [ResponseEntity] containing a list of [ProductResponse] and an HTTP status of OK.
     */
    @GetMapping("/all")
    fun getAllProducts(): ResponseEntity<List<ProductResponse>> {
        val products = productService.getAllProducts()
        val response = ResponseEntity(products, HttpStatus.OK)
        return response
    }

    /**
     * Retrieves a single product by its unique identifier.
     *
     * @param id The unique ID of the product to retrieve.
     * @return A [ResponseEntity] containing the [ProductResponse] of the found product and an HTTP status of OK.
     */
    @GetMapping("/{id}")
    fun getProductById(@PathVariable("id") id: Long): ResponseEntity<ProductResponse> {
        val product = productService.getProductById(id)
        val response = ResponseEntity(product, HttpStatus.OK)
        return response
    }

    /**
     * Retrieves a list of products filtered by a specific category ID.
     *
     * @param id The ID of the category to filter products by.
     * @return A [ResponseEntity] containing a list of [ProductResponse] for products in the specified category and an HTTP status of OK.
     */
    @GetMapping("/by-category") // Changed path to avoid ambiguity
    fun getAllProductsByCategory(
        @RequestParam("categoryId") id: Long, // 'required = true' is default for @RequestParam
    ): ResponseEntity<List<ProductResponse>> {
        val products = productService.getProductsByCategoryId(id)
        val response = ResponseEntity(products, HttpStatus.OK)
        return response
    }

    /**
     * Retrieves a list of products filtered by a specific product name.
     *
     * @param name The name of the product to filter by.
     * @return A [ResponseEntity] containing a list of [ProductResponse] for products matching the specified name and an HTTP status of OK.
     */
    @GetMapping("/by-name") // Changed path to avoid ambiguity
    fun getAllProductsByName(
        @RequestParam("name") name: String
    ): ResponseEntity<List<ProductResponse>> {
        val products = productService.getProductsByName(name)
        val response = ResponseEntity(products, HttpStatus.OK)
        return response
    }

    /**
     * Retrieves a list of products filtered by a specific brand.
     *
     * @param brand The brand name to filter products by.
     * @return A [ResponseEntity] containing a list of [ProductResponse] for products from the specified brand and an HTTP status of OK.
     */
    @GetMapping("/by-brand") // Changed path to avoid ambiguity
    fun getAllProductsByBrand(
        @RequestParam("brand") brand: String
    ): ResponseEntity<List<ProductResponse>> {
        val products = productService.getProductsByBrand(brand)
        val response = ResponseEntity(products, HttpStatus.OK)
        return response
    }

    /**
     * Retrieves a list of products filtered by both brand and name.
     *
     * @param brand The brand name to filter products by.
     * @param name The name of the product to filter by.
     * @return A [ResponseEntity] containing a list of [ProductResponse] for products matching both brand and name, and an HTTP status of OK.
     */
    @GetMapping("/by-brand-and-name") // Changed path to avoid ambiguity
    fun getProudctByBrandAndName(
        @RequestParam("brand") brand: String,
        @RequestParam("name") name: String,
    ): ResponseEntity<List<ProductResponse>> {
        val products = productService.getProductsByBrandAndName(brand, name)
        val response = ResponseEntity(products, HttpStatus.OK)
        return response
    }

    /**
     * Adds a new product to the system.
     *
     * @param createProductRequest The request body containing the details of the product to be created.
     * @return A [ResponseEntity] containing the [ProductResponse] of the newly created product and an HTTP status of CREATED.
     */
    @PostMapping("/add")
    fun addProduct(
        @RequestBody createProductRequest: CreateProductRequest
    ): ResponseEntity<ProductResponse> {
        val product = productService.saveProduct(createProductRequest)
        return ResponseEntity(product, HttpStatus.CREATED)
    }

    /**
     * Updates an existing product identified by its ID.
     *
     * @param id The unique ID of the product to update.
     * @param updateProductRequest The request body containing the updated details for the product.
     * @return A [ResponseEntity] containing the [ProductResponse] of the updated product and an HTTP status of OK.
     */
    @PutMapping("/{id}/update")
    fun updateProduct(
        @PathVariable("id") id: Long,
        @RequestBody updateProductRequest: UpdateProductRequest
    ): ResponseEntity<ProductResponse> {
        val product = productService.updateProduct(id, updateProductRequest)
        return ResponseEntity(product, HttpStatus.OK)
    }

    /**
     * Deletes a product from the system by its unique identifier.
     *
     * @param id The unique ID of the product to delete.
     * @return A [ResponseEntity] with an HTTP status of NO_CONTENT, indicating successful deletion.
     */
    @DeleteMapping("/{id}/delete")
    fun deleteProduct(@PathVariable("id") id: Long): ResponseEntity<Unit> {
        val product = productService.deleteProduct(id)
        return ResponseEntity(product, HttpStatus.NO_CONTENT)
    }
}
