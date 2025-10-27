package com.example.tradeapp.damin.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



@Serializable
data class Asset(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("symbol") val symbol: String? = null,
    // You mentioned adding a link to the asset
    @SerialName("asset_link") val assetLink: String? = null,
    @SerialName("logo_url") val logoUrl: String? = null,
    @SerialName("secondary_image_url") val secondaryImageUrl: String? = null,
    @SerialName("is_active") val isActive: Boolean? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val lastModified: String? = null
)








