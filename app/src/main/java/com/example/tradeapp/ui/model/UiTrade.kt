package com.example.tradeapp.ui.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class UiTrade(
    val quantity: Double? = null,
    val name: String? = null,
    val symbol: String? = null,
    val isActive: Boolean? = null,
    val price: String  = ""
)