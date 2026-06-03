package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.dto.AssetDto
import com.example.tradeapp.dto.HistoryData
import com.example.tradeapp.viewmodel.TimeRange

data class PriceInfo(
    val currentPrice: Double,
    val priceChange: Double,
    val priceChangePercent: Double
)

data class KeyStats(
    val openPrice: Double,
    val closePrice: Double,
    val highPrice: Double,
    val lowPrice: Double,
    val volume: Double,
    val totalTrades: Int,
    val avgPrice: Double
)

// ChartState.kt
data class ChartState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val historyData: HistoryData? = null,
    val asset: AssetDto? = null, // فقط نام، لوگو، سیمبل
    val priceInfo: PriceInfo? = null, // محاسبه شده از HistoryData
    val keyStats: KeyStats? = null, // محاسبه شده از HistoryData
    val selectedTimeRange: TimeRange = TimeRange.ONE_DAY,
    val error: String? = null
)