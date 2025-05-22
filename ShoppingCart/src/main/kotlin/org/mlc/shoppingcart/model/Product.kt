package org.mlc.shoppingcart.model

import jakarta.annotation.Nullable
import jakarta.persistence.*


/**
 * Represents a product in the shopping cart application.
 *
 * This data class is an `@Entity` managed by JPA, mapping to a database table.
 * It contains details about a product, including its unique identifier,
 * name, brand, description, price, available inventory, associated images,
 * and the category it belongs to.
 *
 * @property id The unique identifier of the product. It's an auto-generated primary key.
 * @property name The name of the product.
 * @property brand The brand of the product.
 * @property description A detailed description of the product.
 * @property price The price of the product.
 * @property inventory The current stock quantity of the product.
 * @property images A list of [Image] entities associated with this product.
 * Changes to images (e.g., adding or removing) will cascade to the database.
 * If an image is removed from this list, it will be removed from the database.
 * @property category The [Category] this product belongs to. It's a many-to-one relationship,
 * meaning many products can belong to one category. The fetching strategy is lazy,
 * and changes to the category (e.g., saving a new category) will cascade.
 */
@Entity
data class Product(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,  //  Placeholder for creation of Product instances. DB will assign id to every new entry.
    var name: String,
    var brand: String,
    @Nullable
    var description: String?,
    var price: Double,
    var inventory: Int,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    val images: List<Image> = mutableListOf<Image>(),

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "category_id")
    var category: Category,
)