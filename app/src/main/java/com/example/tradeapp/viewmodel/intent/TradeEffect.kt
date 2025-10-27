package com.example.tradeapp.viewmodel.intent

// TradeEffect.kt
sealed class TradeEffect {
    data class ShowError(val message: String) : TradeEffect()
    data class ShowSuccess(val message: String) : TradeEffect()
    object NavigateBack : TradeEffect()
    data class NavigateToPayment(val url: String) : TradeEffect()
}