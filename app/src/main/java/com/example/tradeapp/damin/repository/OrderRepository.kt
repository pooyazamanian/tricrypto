package com.example.tradeapp.damin.repository

import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.damin.util.Result
import java.util.UUID

interface OrderRepository {
    suspend fun getOrdersForUser(userId: UUID): Result<List<Order>>
    suspend fun createOrder(order: Order): Result<List<Order>>
    suspend fun updateOrderStatus(orderId: UUID, newStatus: String): Result<List<Order>>
    suspend fun deleteOrder(orderId: UUID): Result<Unit>
}