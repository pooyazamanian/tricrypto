package com.example.tradeapp.viewmodel.intent

sealed interface WatchlistIntent {

    data object Load : WatchlistIntent

    data class RefreshPrices(
        val symbols: List<String>
    ) : WatchlistIntent
}
