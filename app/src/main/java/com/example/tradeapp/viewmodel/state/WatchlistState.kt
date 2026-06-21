package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.models.UserWatchlist
import com.example.tradeapp.viewmodel.util.UiStateWithCatch

data class WatchlistState(
    val watchlistData: UiStateWithCatch<List<UserWatchlist>> = UiStateWithCatch.Idle,
    val isRefreshing: Boolean = false
)