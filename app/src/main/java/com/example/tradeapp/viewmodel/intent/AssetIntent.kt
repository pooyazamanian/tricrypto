package com.example.tradeapp.viewmodel.intent

sealed interface AssetIntent {
    object LoadAssets : AssetIntent
}