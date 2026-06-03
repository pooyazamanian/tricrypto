package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.models.MarketTrend

data class MarketTrendsState(
    val isLoading: Boolean = false,
    val trends: List<MarketTrend> = emptyList(),
    val error: String? = null
)

sealed interface MarketTrendsIntent {

    data object Load : MarketTrendsIntent
}