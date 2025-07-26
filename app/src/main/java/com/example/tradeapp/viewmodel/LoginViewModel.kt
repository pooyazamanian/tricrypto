package com.example.tradeapp.viewmodel

import android.credentials.GetCredentialRequest
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.database.SecureStorage
import com.example.tradeapp.damin.repository.AuthenticationRepository
import com.example.tradeapp.damin.repository.AuthenticationRepositoryImpl
import com.example.tradeapp.ui.tools.TableName
import com.example.tradeapp.utils.NamePage
import com.example.tradeapp.utils.sealedClasses.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
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
    private val supabase: SupabaseClient,
    private val secureStorage: SecureStorage
) :ViewModel(){
    var state = mutableStateOf(NamePage.SPLASHSCREEN)
//    init {
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
            state.value = if (session != null && secureStorage.getString(TableName.AUTH_TOKEN) != null) {
                NamePage.BASE_PAGE
            } else {
                NamePage.LOGIN
            }
        }
    }

    fun signUpWithEmail(emailValue: String, passwordValue: String): Flow<AuthResponse> = flow {
        try {
            val user = supabase.auth.signUpWith(Email) {
                email = emailValue
                password = passwordValue
            }
//            supabase.auth.sessionStatus.collect {
//                when(it) {
//                    is SessionStatus.Authenticated -> {
//                        println("Received new authenticated session.")
//                        when(it.source) { //Check the source of the session
//                            SessionSource.External -> TODO()
//                            is SessionSource.Refresh -> TODO()
//                            is SessionSource.SignIn -> TODO()
//                            is SessionSource.SignUp -> {
//                                Log.d("auth", "sign up with supabase auth")
//                            }
//                            SessionSource.Storage -> TODO()
//                            SessionSource.Unknown -> TODO()
//                            is SessionSource.UserChanged -> TODO()
//                            is SessionSource.UserIdentitiesChanged -> TODO()
//                            SessionSource.AnonymousSignIn -> TODO()
//                        }
//                    }
//                    SessionStatus.Initializing -> println("Initializing")
//                    is SessionStatus.RefreshFailure -> {
//                        println("Session expired and could not be refreshed")
//                    }
//                    is SessionStatus.NotAuthenticated -> {
//                        if(it.isSignOut) {
//                            println("User signed out")
//                        } else {
//                            println("User not signed in")
//                        }
//                    }
//                }
//            }
            Log.d("auth", user.toString())
            if(user != null){
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
            }else{
                // Handle email confirmation case, e.g., emit a different response
                emit(AuthResponse.Error("Email confirmation required. Check your email."))
                return@flow
            }
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            Log.e("auth", e.localizedMessage.toString())

            emit(AuthResponse.Error(e.localizedMessage))
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

            state.value = NamePage.LOGIN
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