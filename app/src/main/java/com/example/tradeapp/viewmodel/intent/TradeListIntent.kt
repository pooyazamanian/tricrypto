package com.example.tradeapp.viewmodel.intent

// TradeIntent.kt
sealed interface TradeListIntent {
    object LoadTrades : TradeListIntent

    object ClearTrades: TradeListIntent
}