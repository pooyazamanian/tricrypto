package com.example.tradeapp.damin.model
import kotlinx.serialization.Serializable

@Serializable
data class HistoryData(
    val o: List<Double>, // Open prices
    val c: List<Double>, // Close prices
    val h: List<Double>, // High prices
    val l: List<Double>, // Low prices
    val t: List<Long>, // Timestamps
    val v: List<Double>, // Volume
    val s: String
)
