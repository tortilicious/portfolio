package org.mlc.shoppingcart.utils

/**
 * Represents the various possible states or statuses an order can have within the e-commerce system.
 *
 * This enum helps in tracking the lifecycle of an order from its creation until fulfillment or cancellation,
 * providing clear, standardized, and type-safe representations of an order's progress.
 *
 * Each status signifies a specific stage in the order processing workflow.
 */
enum class OrderStatus {
    /**
     * The initial state of an order immediately after it has been placed by the customer.
     * The payment might be pending authorization or awaiting confirmation.
     * The order has not yet been processed by the fulfillment team.
     */
    PENDING,

    /**
     * The order payment has been successfully authorized or captured, and the order is now
     * being prepared for shipment. This includes tasks like picking items from inventory,
     * packaging, and preparing shipping labels.
     */
    PROCESSING,

    /**
     * The order has been handed over to the shipping carrier (e.g., postal service, courier).
     * It is now en route to the customer. A tracking number might be available.
     */
    SHIPPED,

    /**
     * The order has successfully reached the customer at the specified delivery address.
     * This marks the completion of the fulfillment process.
     */
    DELIVERED,

    /**
     * The order has been canceled. This could be initiated by the customer (e.g., before shipment)
     * or by the system (e.g., due to stock issues or payment failure).
     */
    CANCELLED
}