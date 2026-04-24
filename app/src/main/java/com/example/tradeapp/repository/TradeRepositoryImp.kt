package com.example.tradeapp.repository

import android.util.Log
import com.example.tradeapp.damin.model.Trade
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.damin.api.SupabaseClientWrapper
import com.example.tradeapp.damin.repository.TradeRepository
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.toJsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TradeRepositoryImp @Inject constructor(
    private val supabaseClient: SupabaseClientWrapper
) : TradeRepository {

    // ---------- Trades ----------
    @OptIn(SupabaseInternal::class)
    override suspend fun getTradesForAsset(assetId: UUID, limit: Int): Result<List<Trade>> =
        withContext(Dispatchers.IO) {
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

    override suspend fun getTrades(limit: Int): Result<List<Trade>> =
        withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.get(
                "trades"
            )
            Result.Success(res.decodeList<Trade>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }


    override suspend fun createTrade(trade: Trade): Result<List<Trade>> =
        withContext(Dispatchers.IO) {
            try {
                val res = supabaseClient.post("trades", listOf(trade))
                Result.Success(res.decodeList<Trade>())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }


}