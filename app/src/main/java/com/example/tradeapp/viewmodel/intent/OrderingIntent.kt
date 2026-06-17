package com.example.tradeapp.viewmodel.intent

import com.example.tradeapp.dto.AssetDto
import com.example.tradeapp.models.Order
import com.example.tradeapp.viewmodel.state.OrderType

sealed interface OrderingIntent {
    data class SelectAsset(val asset: AssetDto) : OrderingIntent
    data class SetTradeType(val type: OrderType) : OrderingIntent

    //    data class CreateTrade(val trade: TradeDto) : OrderingIntent
    data class CreateOrder(val order: Order) : OrderingIntent

}