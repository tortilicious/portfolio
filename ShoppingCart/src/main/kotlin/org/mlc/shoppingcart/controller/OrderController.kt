package org.mlc.shoppingcart.controller

import org.mlc.shoppingcart.dto.order.OrderResponse
import org.mlc.shoppingcart.dto.order.UpdateOrderStatusRequest
import org.mlc.shoppingcart.service.order.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("/{userId}/new-order")
    fun placeOrder(@PathVariable("userId") id: Long): ResponseEntity<OrderResponse> {
        val newOrder = orderService.placeOrder(id)
        return ResponseEntity(newOrder, HttpStatus.CREATED)
    }

    @GetMapping("/your-orders/{orderId}")
    fun getOrder(
        @PathVariable("orderId") orderId: Long,
    ): ResponseEntity<OrderResponse> {
        val order = orderService.getOrderById(orderId)
        return ResponseEntity(order, HttpStatus.OK)
    }

    @GetMapping("/your-orders")
    fun getAllOders(
        @RequestParam(value = "userId") userId: Long,
    ): ResponseEntity<Set<OrderResponse>> {

        val orders = orderService.getAllOrdersByUserId(userId)
        return ResponseEntity(orders, HttpStatus.OK)
    }

    @DeleteMapping("{orderId}")
    fun deleteOrder(@PathVariable("orderId") orderId: Long): ResponseEntity<Void> {
        orderService.deleteOrderById(orderId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PutMapping("/{orderId}/update-status")
    fun updateOrderStatus(
        @RequestBody request: UpdateOrderStatusRequest,
        @PathVariable("orderId") orderId: Long
    ): ResponseEntity<OrderResponse> {
        val updatedOrder = orderService.updateOrderById(orderId, request)
        return ResponseEntity(updatedOrder, HttpStatus.OK)
    }
}

