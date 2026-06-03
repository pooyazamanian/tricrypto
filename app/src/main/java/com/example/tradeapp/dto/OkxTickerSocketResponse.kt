package com.example.tradeapp.dto

import kotlinx.serialization.Serializable

// ============================
// SOCKET DTO
// ============================

@Serializable
data class OkxTickerSocketResponse(
    val data: List<OkxTickerItem> = emptyList()
)
