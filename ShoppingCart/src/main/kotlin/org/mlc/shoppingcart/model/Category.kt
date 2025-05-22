package org.mlc.shoppingcart.model

import jakarta.persistence.*


/**
 * Represents a category for products in the shopping cart application.
 *
 * This data class is an `@Entity` managed by JPA, mapping to a database table.
 * It defines the structure for categorizing products, allowing for organization
 * and easier navigation within the application.
 *
 * @property id The unique identifier of the category. It's an auto-generated primary key.
 * @property name The name of the category (e.g., "Electronics", "Books", "Clothing").
 * @property products A list of [Product] entities that belong to this category.
 * This is a one-to-many relationship, indicating that one category can have many products.
 * The `mappedBy` attribute indicates that the `category` field in the [Product] entity
 * is the owner of the relationship.
 */
@Entity
data class Category(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,

    @OneToMany(mappedBy = "category")
    val products: List<Product>
)