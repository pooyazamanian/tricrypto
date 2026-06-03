package com.example.tradeapp.models

data class UserWatchlist(
    val id: String,
    // asset info (from join)
    val symbol: String,
    val name: String,
    val logoUrl: String? = null,

    // realtime data (from websocket)
    val price: Double? = null,
    val percentChange: Double? = null,

    // user data
    val notes: String? = null,
)