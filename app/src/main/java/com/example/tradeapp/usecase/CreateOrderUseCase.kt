package com.example.tradeapp.usecase
import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.damin.repository.OrderRepository
import com.example.tradeapp.damin.util.Result
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(order: Order): Result<List<Order>> {
        return try {
            repository.createOrder(order)
        } catch (e: Exception) {
            Result.Error(Exception("Failed to create order: ${e.message}", e))
        }
    }
}