package com.example.tradeapp.damin.repository

import com.example.tradeapp.damin.model.Trade
import com.example.tradeapp.damin.util.Result
import java.util.UUID

interface TradeRepository {
    suspend fun getTradesForAsset(assetId: UUID, limit: Int = 50): Result<List<Trade>>
    suspend fun createTrade(trade: Trade): Result<List<Trade>>
}