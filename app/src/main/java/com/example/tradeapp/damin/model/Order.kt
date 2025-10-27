package com.example.tradeapp.damin.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    @SerialName("id") val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("asset_id") val assetId: String? = null,
    @SerialName("order_type") val orderType: String? = null,
    @SerialName("price") val price: Double? = null,
    @SerialName("quantity") val quantity: Double? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val lastModified: String? = null,
    // optional helper fields from DB
    @SerialName("filled_quantity") val filledQuantity: Double? = null,
    @SerialName("remaining_quantity") val remainingQuantity: Double? = null
)

