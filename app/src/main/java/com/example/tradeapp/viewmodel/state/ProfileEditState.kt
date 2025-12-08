package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.damin.model.Profile
import io.github.jan.supabase.auth.user.UserInfo


data class ProfileEditState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val res: Boolean? = null,
    val user: UserInfo? = null,
    val error: String? = null
)