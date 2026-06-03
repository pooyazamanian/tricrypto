package com.example.tradeapp.viewmodel.effect

sealed interface WatchlistEffect {
    data class ShowError(val message: String) : WatchlistEffect
}