package com.example.tradeapp.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OkxTickerItem(

    val instId: String,

    @SerialName("last")
    val last: String,

    @SerialName("open24h")
    val open24h: String,

    @SerialName("high24h")
    val high24h: String,

    @SerialName("low24h")
    val low24h: String
)
