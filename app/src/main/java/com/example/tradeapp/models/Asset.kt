package com.example.tradeapp.models

data class Asset(
    val id: String? = null,
    val name: String? = null,
    val symbol: String? = null,
    // You mentioned adding a link to the asset
    val assetLink: String? = null,
    val logoUrl: String? = null,
    val secondaryImageUrl: String? = null,
    val isActive: Boolean? = null,
)