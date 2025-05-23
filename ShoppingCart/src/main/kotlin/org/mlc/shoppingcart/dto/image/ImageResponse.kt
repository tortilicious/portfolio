package org.mlc.shoppingcart.dto.image

/**
 * Response DTO for image details.
 *
 * This class represents the data returned to the client after image-related operations.
 * It contains metadata about the image and its association with a product, but not the binary data itself.
 *
 * @property id The unique identifier of the image.
 * @property fileName The original name of the image file.
 * @property fileType The MIME type of the image file (e.g., "image/jpeg", "image/png").
 * @property downloadUrl An optional URL where the image can be accessed or downloaded (e.g., from a CDN or a separate API endpoint).
 * @property productId The ID of the product this image is associated with.
 */
data class ImageResponse(
    val id: Long,
    val fileName: String,
    val fileType: String,
    val downloadUrl: String?, // Often, downloadUrl is generated dynamically or stored if using external storage
    val productId: Long // To indicate which product this image belongs to
)