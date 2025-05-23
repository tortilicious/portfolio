package org.mlc.shoppingcart.controller

import org.mlc.shoppingcart.dto.image.ImageResponse
import org.mlc.shoppingcart.service.image.ImageService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/v1/images")
class ImageController(private val imageService: ImageService) {


    @GetMapping("/{id}")
    fun getImageMetadaDataById(@PathVariable("id") imageId: Long): ResponseEntity<ImageResponse> {
        val image = imageService.getImageById(imageId)
        return ResponseEntity(image, HttpStatus.OK)
    }

    @GetMapping("/{id}/download")
    fun downLoadImage(@PathVariable("id") imageId: Long): ResponseEntity<ByteArray> {
        val imageMetadata = imageService.getImageById(imageId)
        val imageData = imageService.getImageDataById(imageId)
        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType(imageMetadata.fileType)
        return ResponseEntity(imageData, headers, HttpStatus.OK)
    }

    @PostMapping("/upload")
    fun uploadImage(@RequestParam("files") files: List<MultipartFile>, @RequestParam("productId") id: Long): ResponseEntity<List<ImageResponse>> {
        val image = imageService.saveImages(files, id)
        val response = ResponseEntity(image, HttpStatus.CREATED)
        return response
    }

    @DeleteMapping("/{id}")
    fun deleteImage(@PathVariable("id") imageId: Long): ResponseEntity<Unit> {
        val deletedImage = imageService.deleteImageById(imageId)
        val response = ResponseEntity(deletedImage, HttpStatus.NO_CONTENT)
        return response
    }

    @PutMapping("/{id}")
    fun updateImage(
        @PathVariable id: Long,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<ImageResponse> {
        val response = imageService.updateImage(file, id)
        return ResponseEntity(response, HttpStatus.OK)
    }

}