package com.example.tradeapp.viewmodel.intent

import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.damin.model.Profile
import io.github.jan.supabase.auth.user.UserInfo

sealed class ProfileEditorIntent {
    data class SendProfileDate(val profile: Profile) : ProfileEditorIntent()
    data class EditPhoneUserDate(val phone: String) : ProfileEditorIntent()
    data class ChangeProfileField(val update: Profile) : ProfileEditorIntent()
    data class ChangeUserInfoField(val update: UserInfo) : ProfileEditorIntent()
}