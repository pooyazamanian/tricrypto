package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.damin.model.Asset
import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.damin.model.Profile
import com.example.tradeapp.damin.model.Trade
import com.example.tradeapp.damin.model.UserAsset



data class TradeState(
    val isLoading: Boolean = false,
    val trades: List<Trade> = emptyList(),
    val selectedAsset: Asset? = null,
    val tradeType: TradeType = TradeType.BUY,
    val error: String? = null
)

enum class TradeType {
    BUY, SELL
}