package com.example.tradeapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tradeapp.damin.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {
    val authState = sessionManager.authState
}