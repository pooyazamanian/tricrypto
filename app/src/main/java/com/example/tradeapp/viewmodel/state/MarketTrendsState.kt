package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.models.MarketTrend
import com.example.tradeapp.viewmodel.util.UiStateWithCatch

data class MarketTrendsState(
    val trendsData: UiStateWithCatch<List<MarketTrend>> = UiStateWithCatch.Idle
)

sealed interface MarketTrendsIntent {

    data object Load : MarketTrendsIntent
}