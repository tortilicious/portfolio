package org.mlc.shoppingcart.service.image

import org.mlc.shoppingcart.dto.image.ImageResponse
import org.springframework.web.multipart.MultipartFile

/**
 * Interface for the image service layer.
 * Defines the business operations available for managing product images.
 */
interface ImageService {

    /**
     * Retrieves an image by its unique identifier.
     *
     * @param id The unique ID of the image to retrieve.
     * @return The [ImageResponse] representing the found image.
     * @throws org.mlc.shoppingcart.error.ImageNotFoundException if no image with the specified ID exists.
     */
    fun getImageById(id: Long): ImageResponse

    /**
     * Deletes an image from the database by its unique identifier.
     *
     * @param id The ID of the image to delete.
     * @throws org.mlc.shoppingcart.error.ImageNotFoundException if no image with the specified ID exists.
     */
    fun deleteImageById(id: Long)

    /**
     * Saves a new image and associates it with a product.
     *
     * @param file The [MultipartFile] representing the image data to be saved.
     * @param productId The ID of the product to which the image will be associated.
     * @return The [ImageResponse] of the newly saved image.
     * @throws org.mlc.shoppingcart.error.ProductNotFoundException if the specified product does not exist.
     * @throws org.mlc.shoppingcart.error.ImageProcessingException if an error occurs during image processing or storage.
     */
    fun saveImages(files: List<MultipartFile>, productId: Long): List<ImageResponse>

    /**
     * Updates an existing image associated with a product.
     *
     * This operation typically replaces an existing image for a given product.
     *
     * @param file The [MultipartFile] representing the new image data for the update.
     * @param productId The ID of the product whose image will be updated. This implicitly identifies which image to update (e.g., the primary image, or if a product only has one image).
     * @return The [ImageResponse] of the updated image.
     * @throws org.mlc.shoppingcart.error.ProductNotFoundException if the specified product does not exist.
     * @throws org.mlc.shoppingcart.error.ImageNotFoundException if no image associated with the product is found to update.
     * @throws org.mlc.shoppingcart.error.ImageProcessingException if an error occurs during image processing or storage.
     */
    fun updateImage(file: MultipartFile, imageId: Long): ImageResponse
}