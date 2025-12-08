package com.example.tradeapp.viewmodel.effect

sealed interface TradeEffect {
    data class ShowSuccess(val message: String) : TradeEffect
    data class ShowError(val message: String) : TradeEffect
}