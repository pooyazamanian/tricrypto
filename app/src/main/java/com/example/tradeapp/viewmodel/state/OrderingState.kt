package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.dto.AssetDto

data class OrderingState(
    val isLoading: Boolean = false,
    val selectedAsset: AssetDto? = null,
    val orderType: OrderType = OrderType.BUY,
    val error: String? = null
)

enum class OrderType {
    BUY, SELL
}