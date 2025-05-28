package org.mlc.shoppingcart.model

import jakarta.persistence.*
import java.sql.Blob


/**
 * Represents an image associated with a product in the shopping cart application.
 *
 * This data class is an `@Entity` managed by JPA, mapping to a database table.
 * It stores metadata about the image, the image data itself, and its relationship
 * to a specific product.
 *
 * @property id The unique identifier of the image. It's an auto-generated primary key.
 * @property fileName The original name of the image file.
 * @property fileType The MIME type of the image file (e.g., "image/jpeg", "image/png").
 * @property image The binary data of the image. Stored as a large object (LOB) in the database.
 * @property downloadUrl An optional URL where the image can be accessed or downloaded.
 * @property product The [Product] this image belongs to. It's a many-to-one relationship,
 * meaning many images can be associated with one product. The fetching strategy is lazy.
 */
@Entity
class Image(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    var fileName: String,
    var fileType: String,

    @Lob
    var image: ByteArray,
    var downloadUrl: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}