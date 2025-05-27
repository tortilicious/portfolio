package org.mlc.shoppingcart.service.image

import org.mlc.shoppingcart.dto.image.ImageResponse
import org.mlc.shoppingcart.mapper.*
import org.mlc.shoppingcart.error.ImageNotFoundException
import org.mlc.shoppingcart.error.ImageProcessingException
import org.mlc.shoppingcart.error.ProductNotFoundException
import org.mlc.shoppingcart.model.Image
import org.mlc.shoppingcart.repository.ImageRepository
import org.mlc.shoppingcart.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

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
        val image = imageRepository.getImageById(id)
            ?: throw ImageNotFoundException("Image with id '$id' not found.")
        return image.toImageResponse()
    }

    /**
     * Deletes an image from the database by its unique identifier.
     *
     * @param id The ID of the image to delete.
     * @throws ImageNotFoundException if no image with the specified ID exists.
     */
    @Transactional
    override fun deleteImageById(id: Long) {
        val image = imageRepository.getImageById(id)
            ?: throw ImageNotFoundException("Image with id '$id' not found.")
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
                    image = file.bytes, // Asignación directa de ByteArray
                    product = product,
                    downloadUrl = null
                )

                val savedImageWithId = imageRepository.save(image)

                savedImageWithId.downloadUrl = "/api/v1/images/${savedImageWithId.id}/download"

                val finalSavedImage = imageRepository.save(savedImageWithId)

                savedImageResponses.add(finalSavedImage.toImageResponse())

            } catch (e: IOException) {
                throw ImageProcessingException(
                    "Failed to read image data from file '${file.originalFilename}': ${e.message}"
                )
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
        val imageToUpdate = imageRepository.getImageById(imageId)
            ?: throw ImageNotFoundException("Image with id '$imageId' not found for update.")

        if (file.isEmpty) {
            throw IllegalArgumentException("Cannot update with an empty file.")
        }
        if (file.originalFilename.isNullOrBlank()) {
            throw IllegalArgumentException("File name is missing or empty for update.")
        }

        try {
            imageToUpdate.fileName = file.originalFilename!!
            imageToUpdate.fileType = file.contentType ?: "application/octet-stream"
            imageToUpdate.image = file.bytes
            imageToUpdate.downloadUrl = "/api/v1/images/${imageToUpdate.id}/download"

            val updatedImage = imageRepository.save(imageToUpdate)
            return updatedImage.toImageResponse()

        } catch (e: IOException) {
            throw ImageProcessingException("Failed to read new image data from file: ${e.message}")
        } catch (e: Exception) {
            throw ImageProcessingException("Failed to update image: ${e.message}")
        }
    }

    /**
     * Retrieves the binary data of an image by its unique identifier.
     *
     * @param id The unique ID of the image to retrieve.
     * @return A [ByteArray] containing the binary image data.
     * @throws ImageNotFoundException if no image with the specified ID exists.
     * @throws ImageProcessingException if an error occurs while reading the image data.
     */
    override fun getImageDataById(id: Long): ByteArray {
        val image = imageRepository.getImageById(id)
            ?: throw ImageNotFoundException("Image with id '$id' not found.")

        try {
            return image.image
        } catch (e: Exception) {
            throw ImageProcessingException("Failed to retrieve image data for image with id '$id': ${e.message}")
        }
    }
}