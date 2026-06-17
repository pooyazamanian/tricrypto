package com.example.tradeapp.repository

import android.util.Log
import com.example.tradeapp.models.Order
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.damin.api.SupabaseClientWrapper
import com.example.tradeapp.damin.repository.OrderRepository
import com.example.tradeapp.dto.CreateOrderDto
import com.example.tradeapp.dto.OpenOrderDto
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonPrimitive
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImp @Inject constructor(
    private val supabaseClient: SupabaseClientWrapper
): OrderRepository {

    // ---------- Orders ----------
    override suspend fun getOrdersForUser(): Result<List<OpenOrderDto>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.get(
                "my_open_orders_view"
            )
            Result.Success(res.decodeList<OpenOrderDto>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    override suspend fun getOrders(
    ): Result<List<OpenOrderDto>> = withContext(Dispatchers.IO) {

        try {

            val res = supabaseClient.get(
                table = "open_orders_view",
            )

            Result.Success(
                res.decodeList<OpenOrderDto>()
            )

        } catch (e: Exception) {

            Result.Error(e)
        }
    }

    override suspend fun createOrder(
        order: CreateOrderDto
    ): Result<Unit> = withContext(
        Dispatchers.IO
    ) {

        try {

            Log.d(
                "OrderRepository",
                "sending dto = $order"
            )

            val res =
                supabaseClient.rpc(
                    functionName = "create_order",
                    params = mapOf(
                        "p_asset_id" to JsonPrimitive(order.assetId),
                        "p_order_type" to JsonPrimitive(order.orderType),
                        "p_price" to JsonPrimitive(order.price),
                        "p_quantity" to JsonPrimitive(order.quantity)
                    )
                )

            Log.d(
                "OrderRepository",
                "rpc success = $res"
            )

            Result.Success(Unit)

        } catch (e: Exception) {

            Log.e(
                "OrderRepository",
                "rpc failed",
                e
            )

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