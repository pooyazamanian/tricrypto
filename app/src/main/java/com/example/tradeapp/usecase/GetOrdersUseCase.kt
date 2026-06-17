package com.example.tradeapp.usecase

import com.example.tradeapp.damin.repository.OrderRepository
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.dto.toOrders
import com.example.tradeapp.models.Order
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(): Result<List<Order>> {
        return try {
            when (val result = repository.getOrders()) {
                is Result.Success -> {
                    Result.Success(
                        result.data.toOrders()
                    )
                }
                is Result.Error -> {
                    result
                }
            }
        } catch (e: Exception) {
            Result.Error(Exception("Failed to fetch orders: ${e.message}", e))
        }
    }
}