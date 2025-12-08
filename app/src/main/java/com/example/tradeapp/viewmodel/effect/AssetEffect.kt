package com.example.tradeapp.viewmodel.effect

sealed interface AssetEffect {
    data class ShowError(val message: String) : AssetEffect
}