package org.mlc.shoppingcart.repository

import org.mlc.shoppingcart.model.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository interface for managing [Image] entities.
 * Extends [JpaRepository] to provide standard CRUD operations for images,
 * with [Long] as the type of the ID.
 */
@Repository
interface ImageRepository : JpaRepository<Image, Long> {
    /**
     * Retrieves an [Image] entity by its unique identifier.
     *
     * @param imageId The unique ID of the image to retrieve.
     * @return The [Image] entity if found, or `null` if no image with the given ID exists.
     */
    fun getImageById(imageId: Long): Image?
}