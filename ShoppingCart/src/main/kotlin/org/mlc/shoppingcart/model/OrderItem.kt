package org.mlc.shoppingcart.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Represents an individual item within a customer's [Order].
 *
 * An `OrderItem` captures the precise details of a specific product at the exact moment
 * it was ordered. This includes its quantity, the unit price at that time, and the calculated
 * total price for that particular item. Storing these values directly within the `OrderItem`
 * is crucial for maintaining historical accuracy, even if product catalog prices change later.
 *
 * This class is an `@Entity` managed by JPA, mapping directly to the "order_items" database table.
 */
@Entity
@Table(name = "order_items")
data class OrderItem(
    /**
     * The unique identifier for the order item.
     * It's an auto-generated primary key assigned by the database upon persistence.
     * Defaults to 0L for new, unpersisted entities.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    /**
     * The product associated with this order item.
     * This is a many-to-one relationship, indicating that multiple order items can refer to the same product.
     *
     * - `fetch = FetchType.LAZY`: The [Product] entity is loaded from the database only
     * when this `product` reference is first accessed. This is the recommended strategy to prevent
     * over-fetching data unless the product details are always immediately needed.
     * - `@JoinColumn(name = "product_id", nullable = false)`: Defines the foreign key column
     * in the "order_items" table that links to the "products" table. `nullable = false` ensures
     * every order item must always be associated with a product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    /**
     * The quantity of the product included in this order item.
     */
    val quantity: Int,

    /**
     * The unit price of the product at the time the order was placed.
     * This value is explicitly stored to preserve the historical price, making it independent of
     * the product's current price in the catalog.
     * Stored with 10 digits of precision and 2 decimal places for accurate financial calculations.
     */
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    val unitPrice: BigDecimal,

    /**
     * The total price for this specific order item.
     * This is calculated as `unitPrice * quantity` and stored for convenience and historical accuracy.
     * It is set during object initialization and updated via the [updateTotalPrice] extension function.
     * Stored with 10 digits of precision and 2 decimal places.
     */
    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    var totalPrice: BigDecimal = BigDecimal.ZERO,

    /**
     * The [Order] to which this item belongs.
     * This is a many-to-one relationship, linking the order item back to its parent order.
     * This side is the owning side of the relationship and manages the `order_id` foreign key.
     *
     * - `fetch = FetchType.LAZY`: The [Order] entity is loaded from the database only
     * when this `order` reference is first accessed. This is crucial for avoiding deep
     * recursive loading of the entire order graph.
     * - `@JoinColumn(name = "order_id", nullable = false)`: Defines the foreign key column
     * in the "order_items" table that links to the "orders" table. `nullable = false` ensures
     * an order item must always belong to a parent order.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order?
) {
    /**
     * Initializes the [totalPrice] property when an [OrderItem] object is created.
     * It calculates the total based on [unitPrice] and [quantity], applying standard rounding.
     */
    init {
        updateTotalPrice() // Call the extension function to set the initial total price
    }

    /**
     * Provides a concise string representation of the [OrderItem] entity.
     * This override is manually implemented to prevent potential `StackOverflowError`s,
     * which can occur when auto-generated `toString()` methods of `data class` entities
     * attempt to traverse bidirectional relationships recursively.
     *
     * The implementation avoids accessing the `product` or `order` objects directly
     * to prevent triggering lazy loading or further recursive calls, instead using their IDs.
     */
    override fun toString(): String {
        return "OrderItem(id=$id, productId=${product.id}, quantity=$quantity, unitPrice=$unitPrice, totalPrice=$totalPrice, orderId=${order.id})"
    }

    init {
        updateTotalPrice()
    }

}

/**
 * Extension function for [OrderItem] to calculate and update its [totalPrice].
 * This function ensures the total price is derived correctly from [unitPrice] and [quantity],
 * applying standard rounding to two decimal places, which is crucial for financial accuracy.
 * This should be called whenever [quantity] or [unitPrice] changes.
 */
fun OrderItem.updateTotalPrice() {
    // Corrected calculation: setScale applies to the BigDecimal(quantity) before multiplication,
    // or to the final result, but typically you want intermediate precision or final precision.
    // For simple multiplication, applying to the result is fine.
    totalPrice = unitPrice.multiply(BigDecimal(quantity))
        .setScale(2, RoundingMode.HALF_UP)
}

