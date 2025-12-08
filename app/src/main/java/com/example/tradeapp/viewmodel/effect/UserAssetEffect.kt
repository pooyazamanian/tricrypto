package com.example.tradeapp.viewmodel.effect

sealed interface UserAssetEffect {
    data class ShowError(val message: String) : UserAssetEffect
}