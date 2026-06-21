package com.example.tradeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.session.SessionManager
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.usecase.GetProfileUseCase
import com.example.tradeapp.viewmodel.intent.ProfileIntent
import com.example.tradeapp.viewmodel.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    val userId: UUID? by lazy {
        sessionManager.getCurrentUser()?.id?.let { UUID.fromString(it) }
    }

    init {
        handleIntent(ProfileIntent.LoadProfile)
        handleIntent(ProfileIntent.LoadUser)
    }

    fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LoadProfile -> {
                loadProfile()
            }

            ProfileIntent.LoadUser -> {
                loadUser()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.logout()
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            fetchUser()
            fetchProfile()
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            fetchUser()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            fetchProfile()
        }
    }

    private suspend fun fetchUser() {
        val user = sessionManager.getCurrentUser()

        if (user != null) {
            _state.update {
                it.copy(
                    user = user,
                    error = null
                )
            }
        } else {
            _state.update {
                it.copy(
                    error = "دیتا سمت سرور دریافت نشد"
                )
            }
        }
    }

    private suspend fun fetchProfile() {
        userId?.let { id ->
            when (val result = getProfileUseCase(id)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            profile = result.data,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(error = result.exception.message)
                    }
                }
            }
        }
    }
}