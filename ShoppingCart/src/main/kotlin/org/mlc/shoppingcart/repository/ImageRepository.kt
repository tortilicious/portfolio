package org.mlc.shoppingcart.repository

import org.mlc.shoppingcart.dto.image.ImageResponse
import org.mlc.shoppingcart.model.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, Long> {
    fun findImageByIdOrNull(imageId: Long): Image?
}