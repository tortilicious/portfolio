package org.mlc.shoppingcart.model

import jakarta.persistence.*


@Entity
data class Category(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,

    @OneToMany(mappedBy = "category")
    val products: List<Product>
)
