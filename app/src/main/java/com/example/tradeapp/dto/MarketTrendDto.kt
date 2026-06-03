package com.example.tradeapp.dto

import com.example.tradeapp.models.MarketTrend
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarketTrendDto(
    @SerialName("id") val id: String? = null,
    @SerialName("asset_id") val assetId: String? = null,
    @SerialName("trend_type") val trendType: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("rank") val rank: Int? = null,
    @SerialName("score") val score: Double? = null,
    @SerialName("is_active") val isActive: Boolean? = null,
    @SerialName("starts_at") val startsAt: String? = null,
    @SerialName("ends_at") val endsAt: String? = null,
    @SerialName("created_by") val createdBy: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)
fun MarketTrendDto.toDomain(): MarketTrend {
    return MarketTrend(
        assetId = assetId,
        trendType = trendType,
        title = title,
        rank = rank,
        isActive = isActive,
        price = null
    )
}

fun List<MarketTrendDto>.toDomain() =
    map { it.toDomain() }