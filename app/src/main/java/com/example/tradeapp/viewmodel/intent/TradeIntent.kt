package com.example.tradeapp.viewmodel.intent

import com.example.tradeapp.dto.AssetDto
import com.example.tradeapp.dto.TradeDto
import com.example.tradeapp.viewmodel.state.TradeType

// TradeIntent.kt
sealed interface TradeIntent {
    data class SelectAsset(val asset: AssetDto) : TradeIntent
    data class SetTradeType(val type: TradeType) : TradeIntent
//    data class ObserveTradesForAsset(val assetId: String) : TradeIntent
    object LoadTrades : TradeIntent

    data class CreateTrade(val trade: TradeDto) : TradeIntent
    object ClearTrades: TradeIntent
}