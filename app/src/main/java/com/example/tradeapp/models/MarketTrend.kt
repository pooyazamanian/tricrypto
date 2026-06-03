package com.example.tradeapp.models

data class MarketTrend(
    val assetId: String? = null,
    val trendType: String? = null,
    val title: String? = null,
    val rank: Int? = null,
    val isActive: Boolean? = null,
    val price: Double? = null
)