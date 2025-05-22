package org.mlc.shoppingcart.model

import jakarta.persistence.*


@Entity
data class Product(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    val brand: String,
    val description: String,
    val price: Double,
    val inventory: Int,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    val images: List<Image>,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "category_id")
    val category: Category,
)
