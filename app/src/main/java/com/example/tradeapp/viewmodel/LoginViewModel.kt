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
import com.example.tradeapp.utils.sealedClasses.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val supabase: SupabaseClient,
    private val secureStorage: SecureStorage
) :ViewModel(){
    // استفاده از StateFlow به جای mutableStateOf
    private val _state = MutableStateFlow(NamePage.SPLASHSCREEN)
    val state: StateFlow<String> = _state.asStateFlow()//    init {
//        viewModelScope.launch {
//            // همین‌جا session فعلی رو چک می‌کنیم
//            val session = supabase.auth.currentSessionOrNull()
//            state.value = if (session != null) NamePage.BASE_PAGE else NamePage.LOGIN
//        }
//    }

    fun getSupaBase() = supabase

    init {
        viewModelScope.launch {
            val session = supabase.auth.currentSessionOrNull()

            _state.value = if (session != null) {
                NamePage.BASE_PAGE
            } else {
                NamePage.LOGIN
            }
        }
    }

    fun signUpWithEmail(emailValue: String, passwordValue: String) {
        viewModelScope.launch {
            try {
                // توجه: متد signInWith برای لاگین است، نه ثبت‌نام (signUp)
                supabase.auth.signInWith(Email) {
                    email = emailValue
                    password = passwordValue
                }

                // اگر کد به اینجا برسد و Exception ندهد، یعنی لاگین صد درصد موفق بوده است.
                // نیازی نیست دوباره session را چک کنید، چون گاهی آپدیت شدن آن میلی‌ثانیه‌ای طول می‌کشد.
                Log.d("auth", "Login Success! Changing state to BASE_PAGE")
                _state.value = NamePage.BASE_PAGE

            } catch (e: Exception) {
                Log.e("auth", "Login Failed: ${e.localizedMessage}")
            }
        }
    }
    fun signInWithEmail(emailValue: String, passwordValue: String): Flow<AuthResponse> = flow {
        try {
            supabase.auth.signInWith(Email) {
                email = emailValue
                password = passwordValue
            }
            val session = supabase.auth.currentSessionOrNull()
            Log.d("auth", session.toString())
            if (session != null) {
                secureStorage.saveString(TableName.AUTH_TOKEN,session.accessToken)
                secureStorage.saveString(TableName.REFRESH_TOKEN,session.refreshToken)
            } else {
                // Handle email confirmation case, e.g., emit a different response
                emit(AuthResponse.Error("Email confirmation required. Check your email."))
                return@flow
            }
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }


    fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it ->
            str + "%02x".format(it)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            supabase.auth.signOut()
            secureStorage.clearString(TableName.AUTH_TOKEN)
            secureStorage.clearString(TableName.REFRESH_TOKEN)

            _state.value = NamePage.LOGIN
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
}