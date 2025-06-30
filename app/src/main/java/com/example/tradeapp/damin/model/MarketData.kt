package com.example.tradeapp.damin.model

data class MarketData(
    val cryptoId: String,
    val cryptoName: String,
    val livePrice: Double?,
    val maxPrice: Double?,
    val minPrice: Double?,
    val avgPrice: Double?,
    val volume: Double?,
    val rate: Double?,
    val state: String,
    val timestamp: String
)
