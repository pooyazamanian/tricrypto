package com.example.tradeapp.viewmodel.state


import com.example.tradeapp.damin.model.Profile
import io.github.jan.supabase.auth.user.UserInfo


data class ProfileState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val user: UserInfo? = null,
    val error: String? = null
)