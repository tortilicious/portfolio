package org.mlc.shoppingcart.utils

/**
 * Defines the possible roles for users within the e-commerce system.
 * Using an enum provides type safety, prevents typos, and makes role management clearer.
 */
enum class UserRole {
    USER,
    ADMIN,
    SELLER
}