package com.example.tradeapp.damin.session

sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object SessionExpired : AuthState()
    data class Error(val message: String) : AuthState()
}