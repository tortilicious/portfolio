package org.mlc.shoppingcart.model

import jakarta.persistence.*
import org.mlc.shoppingcart.utils.OrderStatus
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant


/**
 * Represents a completed customer order in the e-commerce system.
 *
 * An order captures a precise snapshot of a transaction at a specific point in time,
 * including its items, total amount, current status, and the exact date and time it was placed.
 * Unlike a shopping cart, an order is a permanent, immutable record of a successful purchase.
 *
 * This class is an `@Entity` managed by JPA, mapping directly to the "orders" database table.
 */
@Entity
@Table(name = "orders")
data class Order(
    /**
     * The unique identifier for the order.
     * It's an auto-generated primary key by the database upon creation.
     * Defaults to 0L for new, unpersisted entities.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    /**
     * The [User] who placed this order. This is a many-to-one relationship.
     * `@JoinColumn(name = "user_id")` specifies the foreign key column in the orders table.
     * No cascade type is specified as the lifecycle of a User should be independent of an Order.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    /**
     * The exact date and time when the order was placed.
     * Defaults to the current instant at the time of object creation, ensuring timezone-agnostic storage.
     */
    val date: Instant = Instant.now(),

    /**
     * The current status of the order.
     * This field uses the [OrderStatus] enum, which is stored as a string in the database
     * for better readability and maintainability of database records.
     */
    @Enumerated(EnumType.STRING)
    var status: OrderStatus = OrderStatus.PENDING,

    /**
     * The total monetary amount of the entire order.
     * This sum includes the prices of all associated [OrderItem]s.
     * It's stored with high precision (10 digits total, 2 decimal places) for accurate financial calculations.
     */
    @Column(name = "total_amount", precision = 10, scale = 2)
    var totalAmount: BigDecimal = BigDecimal.ZERO,

    /**
     * A mutable set of [OrderItem]s that belong to this order.
     * This represents a one-to-many relationship: one order can contain many items.
     *
     * - `mappedBy = "order"`: Indicates that the `order` field in the [OrderItem] entity
     * is the owning side of this bidirectional relationship and is responsible for managing the foreign key
     * in the `order_items` table.
     * - `cascade = [CascadeType.ALL]`: Specifies that all persistence operations (e.g., PERSIST, MERGE, REMOVE)
     * performed on this [Order] entity should be cascaded to its associated [OrderItem]s.
     * This ensures that [OrderItem]s are automatically managed when their parent [Order] is manipulated.
     * - `orphanRemoval = true`: A powerful feature that instructs JPA to automatically delete an [OrderItem]
     * from the database if it is removed from this `orderItems` collection. This is highly suitable
     * for child entities like [OrderItem]s, which conceptually cannot exist without their parent [Order].
     * - `fetch = FetchType.LAZY`: Ensures that the collection of [OrderItem]s is loaded from the database
     * only when it is first accessed (e.g., when you iterate over `orderItems`). This is the recommended
     * approach for `@OneToMany` relationships to prevent performance degradation and excessive memory usage.
     */
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var orderItems: MutableSet<OrderItem> = mutableSetOf()
) {
    /**
     * Provides a concise string representation of the [Order] entity.
     * This override is manually implemented to prevent potential `StackOverflowError`s,
     * which can occur with auto-generated `toString()` methods of `data class` entities
     * when they are involved in complex, bidirectional relationships.
     *
     * The implementation avoids traversing the `orderItems` collection directly,
     * ensuring that only basic properties are included.
     */
    override fun toString(): String {
        return "Order(id=$id, date=$date, status=$status, totalAmount=$totalAmount)"
    }
}

fun Order.calculateTotalAmount() {
    totalAmount = orderItems.sumOf { orderItem ->
        orderItem.unitPrice.multiply(
            BigDecimal(orderItem.quantity)
                .setScale(2, RoundingMode.HALF_UP)
        )
    }
}