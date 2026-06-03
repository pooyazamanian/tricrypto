package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.models.UserWatchlist

data class WatchlistState(

    val isLoading: Boolean = false,

    val assets: List<UserWatchlist> = emptyList(),

    val error: String? = null
)