package com.example.tradeapp.repository

import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.damin.api.SupabaseClientWrapper
import com.example.tradeapp.damin.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImp @Inject constructor(
    private val supabaseClient: SupabaseClientWrapper
): OrderRepository {

    // ---------- Orders ----------
    override suspend fun getOrdersForUser(userId: UUID): Result<List<Order>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.get(
                "orders",
                filter = mapOf("user_id" to userId)
            )
            Result.Success(res.decodeList<Order>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createOrder(order: Order): Result<List<Order>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.post("orders", listOf(order))
            Result.Success(res.decodeList<Order>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateOrderStatus(orderId: UUID, newStatus: String): Result<List<Order>> =
        withContext(Dispatchers.IO) {
            try {
                val res = supabaseClient.patch(
                    "orders",
                    filter = mapOf("id" to orderId),
                    data = mapOf("status" to newStatus)
                )
                Result.Success(res.decodeList<Order>())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun deleteOrder(orderId: UUID): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseClient.delete("orders", filter = mapOf("id" to orderId))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}