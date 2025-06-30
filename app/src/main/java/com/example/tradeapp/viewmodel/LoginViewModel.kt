package com.example.tradeapp.viewmodel

import android.credentials.GetCredentialRequest
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.repository.AuthenticationRepository
import com.example.tradeapp.damin.repository.AuthenticationRepositoryImpl
import com.example.tradeapp.utils.NamePage
import com.example.tradeapp.utils.sealedClasses.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.createSupabaseClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonPrimitive
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepositoryImpl,
    private val supabase: SupabaseClient
) :ViewModel(){
    var state = mutableStateOf(NamePage.BASE_PAGE)
//    init {
//        viewModelScope.launch {
//            // همین‌جا session فعلی رو چک می‌کنیم
//            val session = supabase.auth.currentSessionOrNull()
//            state.value = if (session != null) NamePage.BASE_PAGE else NamePage.LOGIN
//        }
//    }

    fun getSupaBase() = supabase

    fun signUpWithEmail(emailValue: String, passwordValue: String): Flow<AuthResponse> = flow {
        try {
            val data = supabase.auth.signUpWith(Email) {
                email = emailValue
                password = passwordValue
            }

            Log.v("data of login ",data.toString())
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }

    fun signInWithEmail(emailValue: String, passwordValue: String): Flow<AuthResponse> = flow {
        try {
            supabase.auth.signInWith(Email) {
                email = emailValue
                password = passwordValue
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