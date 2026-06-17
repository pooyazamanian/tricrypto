package com.example.tradeapp.viewmodel.effect

sealed interface CreatingOrderEffect {
    data class ShowSuccess(val message: String) : CreatingOrderEffect
    data class ShowError(val message: String) : CreatingOrderEffect
}