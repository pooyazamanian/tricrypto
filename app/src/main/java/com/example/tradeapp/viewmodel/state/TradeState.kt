package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.dto.AssetDto
import com.example.tradeapp.dto.TradeDto
import com.example.tradeapp.models.Trade


data class TradeState(
    val isLoading: Boolean = false,
    val trades: List<Trade> = emptyList(),
    val selectedAsset: AssetDto? = null,
    val tradeType: TradeType = TradeType.BUY,
    val error: String? = null
)

enum class TradeType {
    BUY, SELL
}