package org.mlc.shoppingcart.controller

import org.mlc.shoppingcart.dto.image.ImageResponse
import org.mlc.shoppingcart.service.image.ImageService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/v1/images")
class ImageController(private val imageService: ImageService) {


    @PostMapping
    fun saveImages(
        @RequestParam images: List<MultipartFile>,
        @RequestParam productId: Long,
    ): ResponseEntity<List<ImageResponse>>{

        val imageDTOs = imageService.saveImages(images, productId)
        val response = ResponseEntity(imageDTOs, HttpStatus.OK)
        return response
    }

}