package org.mlc.shoppingcart.model

import jakarta.persistence.*
import java.sql.Blob


@Entity
data class Image(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val fileName: String,
    val fileType: String,

    @Lob
    val image: Blob,
    val downloadUrl: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product
)