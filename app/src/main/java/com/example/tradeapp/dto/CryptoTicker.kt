package com.example.tradeapp.dto

data class CryptoTicker(
    val symbol: String,
    val price: Double,
    val percentChange: Double
)
