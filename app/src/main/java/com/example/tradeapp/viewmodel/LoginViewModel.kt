package com.example.tradeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.repository.AuthenticationRepository
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.utils.NamePage
import com.example.tradeapp.viewmodel.state.LoginState
import com.example.tradeapp.viewmodel.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun toggleMode() {
        _state.update {
            it.copy(
                isSignUpMode = !it.isSignUpMode,
                authStatus = UiState.Idle,
                toastMessage = null
            )
        }
    }

    fun submit(emailValue: String, passwordValue: String) {
        if (emailValue.isBlank() || passwordValue.isBlank()) {
            _state.update { it.copy(toastMessage = "ایمیل و رمز عبور نباید خالی باشند") }
            return
        }

        _state.update { it.copy(authStatus = UiState.Loading) }

        viewModelScope.launch {
            val result = if (_state.value.isSignUpMode) {
                authRepository.signUp(emailValue, passwordValue)
            } else {
                authRepository.signIn(emailValue, passwordValue)
            }

            when (result) {
                is Result.Success -> {
                    if (_state.value.isSignUpMode) {
                        _state.update {
                            it.copy(
                                authStatus = UiState.Idle,
                                isSignUpMode = false,
                                toastMessage = "ثبت‌نام موفق! لطفاً ایمیل خود را تایید کرده و Sign in کنید."
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                authStatus = UiState.Success(Unit)
                            )
                        }
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            authStatus = UiState.Error(result.exception.localizedMessage ?: "خطایی رخ داد")
                        )
                    }
                }
            }
        }
    }

    fun clearToastMessage() {
        _state.update { it.copy(toastMessage = null) }
    }

    fun clearAuthError() {
        _state.update { it.copy(authStatus = UiState.Idle) }
    }
}