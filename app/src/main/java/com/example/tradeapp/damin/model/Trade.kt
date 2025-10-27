package com.example.tradeapp.damin.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Trade(
    @SerialName("id") val id: String? = null,
    @SerialName("buy_order_id") val buyOrderId: String? = null,
    @SerialName("sell_order_id") val sellOrderId: String? = null,
    // Postgres numeric -> Double (use BigDecimal if you need exact precision)
    @SerialName("price") val price: Double? = null,
    @SerialName("quantity") val quantity: Double? = null,
    @SerialName("asset_id") val assetId: String? = null,
    // If your DB column is named `timestamp`
    @SerialName("timestamp") val timestamp: String? = null,
    // Common updated timestamp column name
    @SerialName("updated_at") val lastModified: String? = null
)