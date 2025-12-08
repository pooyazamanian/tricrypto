package com.example.tradeapp.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.usecase.GetAssetsUseCase
import com.example.tradeapp.usecase.GetUserAssetsUseCase
import com.example.tradeapp.viewmodel.effect.UserAssetEffect
import com.example.tradeapp.viewmodel.intent.UserAssetIntent
import com.example.tradeapp.viewmodel.state.UserAssetState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserAssetViewModel @Inject constructor(
    private val getUserAssetsUseCase: GetUserAssetsUseCase,
    private val supabase: SupabaseClient
) : ViewModel() {

    private val _state = MutableStateFlow(UserAssetState())
    val state: StateFlow<UserAssetState> = _state.asStateFlow()

    private val _effect = Channel<UserAssetEffect>()
    val effect = _effect.receiveAsFlow()

    private val userId: UUID? by lazy {
        supabase.auth.currentSessionOrNull()?.user?.id?.let { UUID.fromString(it) }
    }

    init {
        handleIntent(UserAssetIntent.LoadUserAssets)
    }

    fun handleIntent(intent: UserAssetIntent) {
        when (intent) {
            UserAssetIntent.LoadUserAssets -> loadUserAssets()
        }
    }

    private fun loadUserAssets() {
        viewModelScope.launch {
            userId?.let { id ->
                _state.update { it.copy(isLoading = true) }
                when (val result = getUserAssetsUseCase(id)) {
                    is Result.Success -> {
                        _state.update {
                            it.copy(isLoading = false, userAssets = result.data, error = null)
                        }
                    }
                    is Result.Error -> {
                        _state.update {
                            it.copy(isLoading = false, error = result.exception.message)
                        }
                        _effect.send(UserAssetEffect.ShowError(result.exception.message ?: "خطا در بارگذاری دارایی‌های کاربر"))
                    }
                }
            }
        }
    }
}