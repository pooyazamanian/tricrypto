package com.example.tradeapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.model.Profile
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.usecase.UpsertProfileUseCase
import com.example.tradeapp.viewmodel.intent.ProfileEditorIntent
import com.example.tradeapp.viewmodel.state.ProfileEditState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileEditorViewModel @Inject constructor(
    private val upsertProfileUseCase: UpsertProfileUseCase,
    private val supabase: SupabaseClient
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileEditState())
    val state: StateFlow<ProfileEditState> = _state.asStateFlow()


    val userId: UUID? by lazy {
        supabase.auth.currentSessionOrNull()?.user?.id?.let { UUID.fromString(it) }
    }

    fun handleIntent(intent: ProfileEditorIntent) {
        when (intent) {
            is ProfileEditorIntent.SendProfileDate -> {
                sendProfileData(intent)
            }

            is ProfileEditorIntent.EditPhoneUserDate -> {
                sendPhoneUserData(intent)
            }

            is ProfileEditorIntent.ChangeProfileField -> {
                applyProfileFieldChange(intent.update)
            }
            is ProfileEditorIntent.ChangeUserInfoField -> {
                applyUserInfoFieldChange(intent.update)
            }
        }
    }
    private fun applyProfileFieldChange(update: Profile) {
        _state.update { current ->
            current.copy(profile = update)
        }
    }

    private fun applyUserInfoFieldChange(update: UserInfo) {
        _state.update { current ->
            current.copy(user = update)
        }
    }
    private fun sendPhoneUserData(intent: ProfileEditorIntent.EditPhoneUserDate) {
        viewModelScope.launch {
            Log.i("ProfileEditorViewModel", "start")
            try {
                val res = supabase.auth.updateUser {
                    this.phone = intent.phone
                }
                Log.i("ProfileEditorViewModel", res.toString())

            } catch (e: Exception) {
                Log.i("ProfileEditorViewModel", e.toString())
                _state.update {
                    it.copy(
                        isLoading = false,
                        res = false,
                        error = " : مشکلی پیش آمده است$e"
                    )
                }
            }

        }
    }


    private fun sendProfileData(intent: ProfileEditorIntent.SendProfileDate) {
        viewModelScope.launch {
            Log.i("ProfileEditorViewModel", "start")
            val res = upsertProfileUseCase(intent.profile)
            when (res) {
                is Result.Error -> {
                    Log.i("ProfileEditorViewModel", res.exception.toString())
                    _state.update {
                        it.copy(
                            isLoading = false,
                            res = false,
                            error = " : مشکلی پیش آمده است${res.exception}"
                        )
                    }
                }

                is Result.Success<*> -> {
                    Log.i("ProfileEditorViewModel", res.data.toString())
                    _state.update {
                        it.copy(
                            isLoading = false,
                            res = true
                        )
                    }
                }
            }
        }
    }

}

