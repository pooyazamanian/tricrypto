package com.example.tradeapp.dto

import com.example.tradeapp.models.UserWatchlist
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserWatchlistWithAssetDto(
    @SerialName("id") val id: String? = null,
//    @SerialName("user_id") val userId: String? = null,
//
//    @SerialName("asset_id") val assetId: String? = null,

    @SerialName("symbol") val symbol: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("logo_url") val logoUrl: String? = null,

    @SerialName("notes") val notes: String? = null,

//    @SerialName("created_at") val createdAt: String? = null,
//    @SerialName("updated_at") val updatedAt: String? = null
)

fun UserWatchlistWithAssetDto.toDomain(): UserWatchlist {
    return UserWatchlist(
        id = id.orEmpty(),
        symbol = symbol.orEmpty(),
        name = name.orEmpty(),
        logoUrl = logoUrl,
        price = null,
        percentChange = null,
        notes = notes,
    )
}

fun List<UserWatchlistWithAssetDto>.toDomain(): List<UserWatchlist> {
    return map { it.toDomain() }
}

