package com.example.tradeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.usecase.GetProfileUseCase
import com.example.tradeapp.viewmodel.intent.ProfileIntent
import com.example.tradeapp.viewmodel.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
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

    private val supabase: SupabaseClient
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()


    val userId: UUID? by lazy {
        supabase.auth.currentSessionOrNull()?.user?.id?.let { UUID.fromString(it) }
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

    private fun loadUser() {
        viewModelScope.launch {
            val session = supabase.auth.currentSessionOrNull()?.user

             if (session != null) {
                 _state.update {
                     it.copy(
                         user = session,
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
    }

    private fun loadProfile() {
        viewModelScope.launch {
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
}