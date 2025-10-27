package com.example.tradeapp.damin.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAsset(
    @SerialName("id") val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("asset_id") val assetId: String? = null,
    @SerialName("quantity") val quantity: Double? = null,
    @SerialName("updated_at") val lastModified: String? = null
)
