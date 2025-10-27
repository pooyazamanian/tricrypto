package com.example.tradeapp.damin.repository

import com.example.tradeapp.damin.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TradeRepository @Inject constructor(
    private val supabaseClient: SupabaseClientWrapper
) {
    // ---------- Assets ----------
    suspend fun getAssets(): Result<List<Asset>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.get("assets")
            Result.Success(res.decodeList<Asset>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun createAsset(asset: Asset): Result<List<Asset>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.post("assets", listOf(asset))
            Result.Success(res.decodeList<Asset>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // ---------- Orders ----------
    suspend fun getOrdersForUser(userId: UUID): Result<List<Order>> = withContext(Dispatchers.IO) {
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

    suspend fun createOrder(order: Order): Result<List<Order>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.post("orders", listOf(order))
            Result.Success(res.decodeList<Order>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateOrderStatus(orderId: UUID, newStatus: String): Result<List<Order>> = withContext(Dispatchers.IO) {
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

    suspend fun deleteOrder(orderId: UUID): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseClient.delete("orders", filter = mapOf("id" to orderId))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // ---------- Profiles ----------
    suspend fun getProfile(userId: UUID): Result<Profile?> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.get(
                "profiles",
                filter = mapOf("id" to userId)
            )
            Result.Success(res.decodeList<Profile>().firstOrNull())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun upsertProfile(profile: Profile): Result<List<Profile>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.post("profiles", listOf(profile))
            Result.Success(res.decodeList<Profile>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // ---------- Trades ----------
    suspend fun getTradesForAsset(assetId: UUID, limit: Int = 50): Result<List<Trade>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.get(
                "trades",
                filter = mapOf("asset_id" to assetId)
            )
            Result.Success(res.decodeList<Trade>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun createTrade(trade: Trade): Result<List<Trade>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.post("trades", listOf(trade))
            Result.Success(res.decodeList<Trade>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // ---------- UserAssets ----------
    suspend fun getUserAssets(userId: UUID): Result<List<UserAsset>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.get(
                "user_assets",
                filter = mapOf("user_id" to userId)
            )
            Result.Success(res.decodeList<UserAsset>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun upsertUserAsset(userAsset: UserAsset): Result<List<UserAsset>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.post("user_assets", listOf(userAsset))
            Result.Success(res.decodeList<UserAsset>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
