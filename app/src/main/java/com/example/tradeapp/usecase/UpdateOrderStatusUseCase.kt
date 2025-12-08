package com.example.tradeapp.usecase
import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.damin.repository.OrderRepository
import com.example.tradeapp.damin.util.Result
import java.util.UUID
import javax.inject.Inject

class UpdateOrderStatusUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: UUID, newStatus: String): Result<List<Order>> {
        return try {
            repository.updateOrderStatus(orderId, newStatus)
        } catch (e: Exception) {
            Result.Error(Exception("Failed to update order status: ${e.message}", e))
        }
    }
}