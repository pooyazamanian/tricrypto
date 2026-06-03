package com.example.tradeapp.damin.repository

import com.example.tradeapp.dto.CryptoTicker
import kotlinx.coroutines.flow.SharedFlow

interface MarketRepository {
    val tickerStream: SharedFlow<Map<String, CryptoTicker>>

    suspend fun connect(
        symbols: List<String>
    )

    suspend fun disconnect()
}