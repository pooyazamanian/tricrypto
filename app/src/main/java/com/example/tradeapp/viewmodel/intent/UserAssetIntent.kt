package com.example.tradeapp.viewmodel.intent

sealed interface UserAssetIntent {
    object LoadUserAssets : UserAssetIntent
}