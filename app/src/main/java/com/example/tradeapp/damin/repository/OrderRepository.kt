package com.example.tradeapp.damin.repository

import com.example.tradeapp.models.Order
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.dto.CreateOrderDto
import com.example.tradeapp.dto.OpenOrderDto
import java.util.UUID

interface OrderRepository {
    suspend fun getOrdersForUser(): Result<List<OpenOrderDto>>
    suspend fun createOrder(order: CreateOrderDto): Result<Unit>
    suspend fun updateOrderStatus(orderId: UUID, newStatus: String): Result<List<Order>>
    suspend fun deleteOrder(orderId: UUID): Result<Unit>
    suspend fun getOrders(): Result<List<OpenOrderDto>>
}