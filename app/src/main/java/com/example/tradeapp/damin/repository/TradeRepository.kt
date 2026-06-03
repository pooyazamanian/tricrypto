package com.example.tradeapp.damin.repository

import com.example.tradeapp.dto.TradeDto
import com.example.tradeapp.damin.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

interface TradeRepository {
    fun observeTradesForAsset(assetId: UUID?=null, limit: Int = 50): StateFlow<List<TradeDto>>
    suspend fun getTrades(limit: Int = 50): Result<List<TradeDto>>

    suspend fun createTrade(trade: TradeDto): Result<List<TradeDto>>
    fun disconnectTrades()
}