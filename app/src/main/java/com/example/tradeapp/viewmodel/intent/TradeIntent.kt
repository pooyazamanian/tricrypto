package com.example.tradeapp.viewmodel.intent

import com.example.tradeapp.damin.model.Asset
import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.damin.model.Trade

// TradeIntent.kt
sealed class TradeIntent {
    object LoadAssets : TradeIntent()
    object LoadUserAssets : TradeIntent()
    object LoadOrders : TradeIntent()
    object LoadProfile : TradeIntent()
    data class LoadTradesForAsset(val assetId: String) : TradeIntent()
    data class CreateOrder(val order: Order) : TradeIntent()
    data class UpdateOrderStatus(val orderId: String, val status: String) : TradeIntent()
    data class DeleteOrder(val orderId: String) : TradeIntent()
    data class SelectAsset(val asset: Asset) : TradeIntent()
    data class SetTradeType(val type: TradeType) : TradeIntent()
    data class CreateTrade(val trade: Trade) : TradeIntent()
}
