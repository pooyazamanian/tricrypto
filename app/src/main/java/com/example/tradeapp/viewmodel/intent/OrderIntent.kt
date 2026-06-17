package com.example.tradeapp.viewmodel.intent

import com.example.tradeapp.models.Order

sealed interface OrderIntent {
    object LoadOrders : OrderIntent
    data class UpdateOrderStatus(val orderId: String, val status: String) : OrderIntent
}