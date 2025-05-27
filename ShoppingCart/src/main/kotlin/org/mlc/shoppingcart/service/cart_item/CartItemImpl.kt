package org.mlc.shoppingcart.service.cart_item

import org.mlc.shoppingcart.repository.CartRepository
import org.springframework.stereotype.Service


@Service
class CartItemImpl(
    private val cartRepository: CartRepository
) : CartItemService {

}