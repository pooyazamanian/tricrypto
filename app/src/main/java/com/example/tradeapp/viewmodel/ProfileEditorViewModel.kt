package com.example.tradeapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.dto.Profile
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.usecase.UpsertProfileUseCase
import com.example.tradeapp.viewmodel.intent.ProfileEditorIntent
import com.example.tradeapp.viewmodel.state.ProfileEditState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class ProfileUiEffect {
    data class ShowError(val message: String) : ProfileUiEffect()
    object SaveSuccess : ProfileUiEffect()
}

@HiltViewModel
class ProfileEditorViewModel @Inject constructor(
    private val upsertProfileUseCase: UpsertProfileUseCase,
    private val supabase: SupabaseClient
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileEditState())
    val state: StateFlow<ProfileEditState> = _state.asStateFlow()

    private val _effect = Channel<ProfileUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleIntent(intent: ProfileEditorIntent) {
        when (intent) {
            is ProfileEditorIntent.SendProfileDate -> sendProfileAndPhoneData()
            is ProfileEditorIntent.EditPhoneUserDate -> {
                // Now handled within sendProfileAndPhoneData as part of the save flow
            }
            is ProfileEditorIntent.ChangeProfileField -> applyProfileFieldChange(intent.update)
            is ProfileEditorIntent.ChangeUserInfoField -> applyUserInfoFieldChange(intent.update)
        }
    }

    private fun applyProfileFieldChange(update: Profile) {
        _state.update { it.copy(profile = update) }
    }

    private fun applyUserInfoFieldChange(update: UserInfo) {
        _state.update { it.copy(user = update) }
    }

    private fun sendProfileAndPhoneData() {
        val currentProfile = _state.value.profile ?: return
        val currentPhone = _state.value.user?.phone

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            try {
                withContext(Dispatchers.IO) {
                    // 1. Update Profile in Database
                    val profileRes = upsertProfileUseCase(currentProfile)
                    if (profileRes is Result.Error) {
                        throw profileRes.exception
                    }

                    // 2. Update Phone in Auth if changed
                    currentPhone?.let { phone ->
                        supabase.auth.updateUser {
                            this.phone = phone
                        }
                    }
                }

                _state.update { it.copy(isLoading = false, res = true) }
                _effect.send(ProfileUiEffect.SaveSuccess)

            } catch (e: Exception) {
                Log.e("ProfileEditorViewModel", "Error updating profile", e)
                val errorMessage = "خطا در بروزرسانی: ${e.localizedMessage ?: "مشکلی پیش آمده است"}"
                _state.update { it.copy(isLoading = false, res = false, error = errorMessage) }
                _effect.send(ProfileUiEffect.ShowError(errorMessage))
            }
        }
    }
}
