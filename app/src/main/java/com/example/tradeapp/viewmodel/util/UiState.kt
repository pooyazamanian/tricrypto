package com.example.tradeapp.viewmodel.util

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String?) : UiState<Nothing>()
}

sealed class UiStateWithCatch<out T> {
    data object Idle : UiStateWithCatch<Nothing>()

    // ✨ اضافه کردن تایم‌استمپ برای تمایز داده کش شده
    data class Loading<T>(
        val cachedData: T? = null,
        val isRefreshing: Boolean = true // true = refresh, false = initial load
    ) : UiStateWithCatch<T>()

    data class Success<T>(
        val data: T,
        val isFromCache: Boolean = false, // نشون میده داده از کش هست یا نه
        val lastUpdated: Long = System.currentTimeMillis()
    ) : UiStateWithCatch<T>()

    data class Error<T>(
        val message: String?,
        val cachedData: T? = null,
        val lastAttempt: Long = System.currentTimeMillis()
    ) : UiStateWithCatch<T>()
}



sealed class UiStateWithInitData<out T> {
    data class Idle<T>(
        val initData: T? = null,
        val isRefreshing: Boolean = false // true = refresh, false = initial load
    ) : UiStateWithInitData<T>()

    // ✨ اضافه کردن تایم‌استمپ برای تمایز داده کش شده
    data class Loading<T>(
        val cachedData: T? = null,
        val isRefreshing: Boolean = true // true = refresh, false = initial load
    ) : UiStateWithInitData<T>()

    data class Success<T>(
        val data: T,
        val isFromCache: Boolean = false, // نشون میده داده از کش هست یا نه
        val lastUpdated: Long = System.currentTimeMillis()
    ) : UiStateWithInitData<T>()

    data class Error<T>(
        val message: String?,
        val cachedData: T? = null,
        val lastAttempt: Long = System.currentTimeMillis()
    ) : UiStateWithInitData<T>()
}



fun <T> UiStateWithInitData<T>.dataOrCached(): T? = when (this) {
    is UiStateWithInitData.Success -> data
    is UiStateWithInitData.Loading -> cachedData
    is UiStateWithInitData.Error -> cachedData
    is UiStateWithInitData.Idle -> initData
}

fun <T> UiStateWithCatch<T>.dataOrCached(): T? = when (this) {
    is UiStateWithCatch.Success -> data
    is UiStateWithCatch.Loading -> cachedData
    is UiStateWithCatch.Error -> cachedData
    UiStateWithCatch.Idle -> null
}


fun <T> UiState<T>.dataOrCached(): T? = when (this) {
    is UiState.Success -> data
    else -> null
}