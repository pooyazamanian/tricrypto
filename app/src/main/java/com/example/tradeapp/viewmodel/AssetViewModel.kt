package com.example.tradeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.usecase.GetAssetsUseCase
import com.example.tradeapp.viewmodel.intent.AssetIntent
import com.example.tradeapp.viewmodel.state.AssetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.viewmodel.effect.AssetEffect
import javax.inject.Inject

@HiltViewModel
class AssetViewModel @Inject constructor(
    private val getAssetsUseCase: GetAssetsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AssetState())
    val state: StateFlow<AssetState> = _state.asStateFlow()

    private val _effect = Channel<AssetEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        handleIntent(AssetIntent.LoadAssets)
    }

    fun handleIntent(intent: AssetIntent) {
        when (intent) {
            AssetIntent.LoadAssets -> loadAssets()
        }
    }

    private fun loadAssets() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getAssetsUseCase()) {
                is Result.Success -> {
                    _state.update {
                        it.copy(isLoading = false, assets = result.data, error = null)
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = result.exception.message)
                    }
                    _effect.send(AssetEffect.ShowError(result.exception.message ?: "خطا در بارگذاری دارایی‌ها"))
                }
            }
        }
    }
}