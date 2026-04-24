package com.example.tradeapp.ui.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.SerialName

data class UiUserAsset(
    val quantity: Double? = null,
    val name: String? = null,
    val symbol: String? = null,
    // You mentioned adding a link to the asset
//     val assetLink: String? = null,
//     val logoUrl: String? = null,
//     val secondaryImageUrl: String? = null,
    val isActive: Boolean? = null,
    val percentage: MutableState<String>  = mutableStateOf("")
)
