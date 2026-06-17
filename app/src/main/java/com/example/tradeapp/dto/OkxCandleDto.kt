package com.example.tradeapp.dto

data class OkxCandleDto(
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double,
    val volumeCurrency: Double,
    val volumeQuote: Double,
    val confirmed: Boolean
)

fun OkxHistoryResponseDto.toHistoryData(): HistoryData {

    val candles =
        toCandles()
            .sortedBy { it.timestamp }

    return HistoryData(
        o = candles.map { it.open },
        c = candles.map { it.close },
        h = candles.map { it.high },
        l = candles.map { it.low },
        t = candles.map { it.timestamp / 1000L },
        v = candles.map { it.volume },
        s = "ok"
    )
}