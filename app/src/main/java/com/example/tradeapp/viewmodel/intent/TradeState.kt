package com.example.tradeapp.viewmodel.intent

import com.example.tradeapp.damin.model.Asset
import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.damin.model.Profile
import com.example.tradeapp.damin.model.Trade
import com.example.tradeapp.damin.model.UserAsset



enum class TradeType {
    BUY, SELL
}


// TradeState.kt
// در TradeState اضافه کن:
data class TradeState(
    val isLoading: Boolean = false,
    val assets: List<Asset> = emptyList(),
    val userAssets: List<UserAsset> = emptyList(),
    val orders: List<Order> = emptyList(),
    val profile: Profile? = null,
    val trades: List<Trade> = emptyList(),
    val error: String? = null,
    val selectedAsset: Asset? = null,
    val tradeType: TradeType = TradeType.BUY,
    // فیلدهای جدید برای قیمت
    val currentPrice: Double? = null,
    val priceChange: Double? = null,
    val priceChangePercent: Double? = null
)