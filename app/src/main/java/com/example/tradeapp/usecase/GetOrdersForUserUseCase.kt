package com.example.tradeapp.usecase

import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.damin.repository.OrderRepository
import java.util.UUID
import javax.inject.Inject

class GetOrdersForUserUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(userId: UUID): Result<List<Order>> {
        return try {
            repository.getOrdersForUser(userId)
        } catch (e: Exception) {
            Result.Error(Exception("Failed to fetch orders: ${e.message}", e))
        }
    }
}