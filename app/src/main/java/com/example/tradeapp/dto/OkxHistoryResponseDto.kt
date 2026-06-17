package com.example.tradeapp.dto

import kotlinx.serialization.Serializable

@Serializable
data class OkxHistoryResponseDto(
    val code: String,
    val msg: String,
    val data: List<List<String>>
)

fun OkxHistoryResponseDto.toCandles(): List<OkxCandleDto> {
    return data.mapNotNull { candle ->

        if (candle.size < 9)
            return@mapNotNull null

        OkxCandleDto(
            timestamp = candle[0].toLong(),
            open = candle[1].toDouble(),
            high = candle[2].toDouble(),
            low = candle[3].toDouble(),
            close = candle[4].toDouble(),
            volume = candle[5].toDouble(),
            volumeCurrency = candle[6].toDouble(),
            volumeQuote = candle[7].toDouble(),
            confirmed = candle[8] == "1"
        )
    }
}