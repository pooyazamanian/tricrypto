package com.example.tradeapp.dto

import com.example.tradeapp.models.MarketTrend
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarketTrendDto(
    @SerialName("id") val id: String? = null,
    @SerialName("trend_type") val trendType: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("rank") val rank: Int? = null,
    @SerialName("score") val score: Double? = null,
    @SerialName("is_active") val isActive: Boolean? = null,
    @SerialName("starts_at") val startsAt: String? = null,
    @SerialName("ends_at") val endsAt: String? = null,
    @SerialName("asset") val asset: AssetDto? = null
)
fun MarketTrendDto.toDomain(): MarketTrend {
    return MarketTrend(
        asset = asset,
        trendType = trendType,
        title = title,
        rank = rank,
        isActive = isActive,
        price = null
    )
}

fun List<MarketTrendDto>.toDomain() =
    map { it.toDomain() }