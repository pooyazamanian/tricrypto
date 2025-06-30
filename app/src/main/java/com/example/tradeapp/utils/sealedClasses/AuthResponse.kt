package com.example.tradeapp.utils.sealedClasses

sealed interface AuthResponse {
    data object Success : AuthResponse
    data class Error(val message: String?) : AuthResponse
}