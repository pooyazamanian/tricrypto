package com.example.tradeapp.viewmodel

import android.credentials.GetCredentialRequest
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.database.SecureStorage
import com.example.tradeapp.repository.AuthenticationRepositoryImpl
import com.example.tradeapp.ui.tools.TableName
import com.example.tradeapp.utils.NamePage
import com.example.tradeapp.viewmodel.util.UiState
import com.example.tradeapp.viewmodel.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val supabase: SupabaseClient,
    private val secureStorage: SecureStorage
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val session = supabase.auth.currentSessionOrNull()
            if (session != null) {
                // کاربر لاگین است -> برو به صفحه اصلی
                _state.update { it.copy(currentPage = NamePage.BASE_PAGE) }
            } else {
                // کاربر لاگین نیست -> برو به صفحه لاگین
                _state.update { it.copy(currentPage = NamePage.LOGIN) }
            }
        }
    }

    fun toggleMode() {
        _state.update {
            it.copy(
                isSignUpMode = !it.isSignUpMode,
                authStatus = UiState.Idle, // ریست کردن ارورهای قبلی نتورک
                toastMessage = null
            )
        }
    }

    fun submit(emailValue: String, passwordValue: String) {
        if (emailValue.isBlank() || passwordValue.isBlank()) {
            _state.update { it.copy(toastMessage = "ایمیل و رمز عبور نباید خالی باشند") }
            return
        }

        // فقط authStatus لودینگ می‌شود، currentPage همون LOGIN می‌ماند تا صفحه غیب نشود!
        _state.update { it.copy(authStatus = UiState.Loading) }

        viewModelScope.launch {
            try {
                if (_state.value.isSignUpMode) {
                    supabase.auth.signUpWith(Email) {
                        email = emailValue
                        password = passwordValue
                    }
                    _state.update {
                        it.copy(
                            authStatus = UiState.Idle,
                            isSignUpMode = false,
                            toastMessage = "ثبت‌نام موفق! لطفاً ایمیل خود را تایید کرده و Sign in کنید."
                        )
                    }
                } else {
                    supabase.auth.signInWith(Email) {
                        email = emailValue
                        password = passwordValue
                    }
                    val session = supabase.auth.currentSessionOrNull()
                    if (session != null) {
                        secureStorage.saveString(TableName.AUTH_TOKEN, session.accessToken)
                        secureStorage.saveString(TableName.REFRESH_TOKEN, session.refreshToken)

                        // لاگین موفق! تغییر Navigation به صفحه اصلی و موفقیت دکمه
                        _state.update {
                            it.copy(
                                authStatus = UiState.Success(Unit),
                                currentPage = NamePage.BASE_PAGE
                            )
                        }
                    } else {
                        _state.update { it.copy(authStatus = UiState.Error("ایمیل تایید نشده است.")) }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(authStatus = UiState.Error(e.localizedMessage ?: "خطایی رخ داد")) }
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
//    private val _email = MutableStateFlow("")
//    val email: Flow<String> = _email
//
//    private val _password = MutableStateFlow("")
//    val password = _password
//
//    fun onEmailChange(email: String) {
//        _email.value = email
//    }
//
//    fun onPasswordChange(password: String) {
//        _password.value = password
//    }
//
//    fun onSignUp() {
//        viewModelScope.launch {
//            authenticationRepository.signUp(
//                email = _email.value,
//                password = _password.value
//            )
//        }
//    }
//
//    fun onSignIn() {
//        viewModelScope.launch {
//            authenticationRepository.signIn(
//                email = _email.value,
//                password = _password.value
//            )
//        }
//    }
//}