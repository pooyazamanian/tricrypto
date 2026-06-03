package com.example.tradeapp.viewmodel.intent

import com.example.tradeapp.dto.Order

sealed interface OrderIntent {
    object LoadOrders : OrderIntent
    data class CreateOrder(val order: Order) : OrderIntent
    data class UpdateOrderStatus(val orderId: String, val status: String) : OrderIntent
}