package com.example.tradeapp.repository

import android.util.Log
import com.example.tradeapp.dto.TradeDto
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.damin.api.SupabaseClientWrapper
import com.example.tradeapp.damin.repository.TradeRepository
import com.example.tradeapp.repository.util.TradeStreamManager
import io.github.jan.supabase.realtime.PostgresAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TradeRepositoryImp @Inject constructor(
    private val supabaseClient: SupabaseClientWrapper,
    private val manager: TradeStreamManager

) : TradeRepository {

    val scope = CoroutineScope(Dispatchers.IO)
    // ---------- Trades ----------
    override fun observeTradesForAsset(
        assetId: UUID?,
        limit: Int
    ): StateFlow<List<TradeDto>> {

        manager.connect(
            scope = scope,
            assetId = assetId,
            limit = limit
        )

        return manager.trades
    }

    override fun disconnectTrades() {
        manager.disconnect()
    }

    override suspend fun getTrades(limit: Int): Result<List<TradeDto>> =
        withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.get(
                "trades"
            )
            Result.Success(res.decodeList<TradeDto>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }


    override suspend fun createTrade(trade: TradeDto): Result<List<TradeDto>> =
        withContext(Dispatchers.IO) {
            try {
                val res = supabaseClient.post("trades", listOf(trade))
                Result.Success(res.decodeList<TradeDto>())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }


}