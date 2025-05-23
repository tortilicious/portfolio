package org.mlc.shoppingcart.service.image

import org.hibernate.engine.jdbc.BlobProxy
import org.mlc.shoppingcart.dto.image.ImageResponse
import org.mlc.shoppingcart.error.ImageNotFoundException
import org.mlc.shoppingcart.error.ImageProcessingException
import org.mlc.shoppingcart.error.ProductNotFoundException
import org.mlc.shoppingcart.mapper.toImageResponse
import org.mlc.shoppingcart.model.Image
import org.mlc.shoppingcart.repository.ImageRepository
import org.mlc.shoppingcart.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException


/**
 * Service implementation for managing [Image] entities.
 *
 * This class handles the business logic for image operations, including
 * saving, retrieving, updating, and deleting images, as well as associating them with products.
 */
@Service
class ImageServiceImpl(
    private val imageRepository: ImageRepository,
    private val productRepository: ProductRepository,
) : ImageService {

    /**
     * Retrieves an image by its unique identifier.
     *
     * @param id The unique ID of the image to retrieve.
     * @return The [ImageResponse] representing the found image.
     * @throws ImageNotFoundException if no image with the specified ID exists.
     */
    override fun getImageById(id: Long): ImageResponse {
        val image =
            imageRepository.findImageByIdOrNull(id) ?: throw ImageNotFoundException("Image with id '$id' not found")
        val response = image.toImageResponse()
        return response
    }

    /**
     * Deletes an image from the database by its unique identifier.
     *
     * @param id The ID of the image to delete.
     * @throws ImageNotFoundException if no image with the specified ID exists.
     */
    @Transactional
    override fun deleteImageById(id: Long) {
        val image =
            imageRepository.findImageByIdOrNull(id) ?: throw ImageNotFoundException("Image with id '$id' not found")
        imageRepository.delete(image)
    }

    /**
     * Saves one or more new images and associates them with a product.
     *
     * @param files A [List] of [MultipartFile] representing the image data to save.
     * @param productId The ID of the product to which the images will be associated.
     * @return A [List] of [ImageResponse] of the newly saved images.
     * @throws ProductNotFoundException if the product does not exist.
     * @throws IllegalArgumentException if the list of files is empty or contains invalid files (empty, missing name).
     * @throws ImageProcessingException if an error occurs during processing or storage of any image.
     */
    @Transactional
    override fun saveImages(files: List<MultipartFile>, productId: Long): List<ImageResponse> {
        if (files.isEmpty()) {
            throw IllegalArgumentException("Cannot save an empty list of files.")
        }

        val product = productRepository.findById(productId)
            .orElseThrow { ProductNotFoundException("Product with id '$productId' not found.") }

        val savedImageResponses = mutableListOf<ImageResponse>()

        files.forEach { file ->
            if (file.isEmpty) {
                throw IllegalArgumentException("One of the files is empty. Cannot save empty files.")
            }
            if (file.originalFilename.isNullOrBlank()) {
                throw IllegalArgumentException("One of the files is missing or has an empty name.")
            }

            try {
                val image = Image(
                    fileName = file.originalFilename!!,
                    fileType = file.contentType ?: "application/octet-stream",
                    image = BlobProxy.generateProxy(file.bytes),
                    product = product,
                    downloadUrl = null
                )

                val savedImageWithId = imageRepository.save(image)

                savedImageWithId.downloadUrl = "/api/v1/images/${savedImageWithId.id}/download"

                val finalSavedImage = imageRepository.save(savedImageWithId)

                savedImageResponses.add(finalSavedImage.toImageResponse())


            } catch (e: IOException) {
                throw ImageProcessingException("Failed to read image data from file '${file.originalFilename}': ${e.message}")
            } catch (e: Exception) {
                throw ImageProcessingException("Failed to save image '${file.originalFilename}': ${e.message}")
            }
        }
        return savedImageResponses
    }

    /**
     * Updates an existing image associated with a product.
     *
     * This operation typically replaces an existing image for a given product.
     *
     * @param file The [MultipartFile] representing the new image data for the update.
     * @param imageId The ID of the image to update.
     * @return The [ImageResponse] of the updated image.
     * @throws ImageNotFoundException if the image to update does not exist.
     * @throws IllegalArgumentException if the file is empty or has a missing/empty name.
     * @throws ImageProcessingException if an error occurs during image processing or storage.
     */
    @Transactional
    override fun updateImage(file: MultipartFile, imageId: Long): ImageResponse {
        val image = imageRepository.findImageByIdOrNull(imageId)
            ?: throw ImageNotFoundException("Image with id '$imageId' not found f or update")
        if (file.isEmpty) throw IllegalArgumentException("Image with id '$imageId' must not be empty")

        try {
            image.fileName =
                file.originalFilename ?: throw IllegalArgumentException("Image with id '$imageId' must not be empty")
            image.fileType = file.contentType ?: "application/octet-stream"
            image.image = BlobProxy.generateProxy((file.bytes))

            val updatedImage = imageRepository.save(image)
            return updatedImage.toImageResponse()

        } catch (e: Exception) {
            throw ImageProcessingException("Failed to read new image data from file: ${e.message}")
        } catch (e: IOException) {
            throw ImageProcessingException("Failed to update image: ${e.message}")
        }

    }
}