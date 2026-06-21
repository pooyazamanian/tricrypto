package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.dto.AssetDto
import com.example.tradeapp.models.Trade


data class TradeListState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val trades: List<Trade> = emptyList(),
    val error: String? = null
)