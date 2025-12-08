package com.example.tradeapp.viewmodel.effect

sealed interface OrderEffect {
    data class ShowSuccess(val message: String) : OrderEffect
    data class ShowError(val message: String) : OrderEffect
}