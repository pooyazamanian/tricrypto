package com.example.tradeapp.viewmodel.intent

import com.example.tradeapp.damin.model.Asset
import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.damin.model.Trade
import com.example.tradeapp.viewmodel.state.TradeType

// TradeIntent.kt
sealed interface TradeIntent {
    data class SelectAsset(val asset: Asset) : TradeIntent
    data class SetTradeType(val type: TradeType) : TradeIntent
    data class LoadTradesForAsset(val assetId: String) : TradeIntent
    object LoadTrades : TradeIntent

    data class CreateTrade(val trade: Trade) : TradeIntent
    object ClearTrades: TradeIntent
}