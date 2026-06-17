package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.utils.NamePage
import com.example.tradeapp.viewmodel.util.UiState

data class LoginState(
    val currentPage: String = NamePage.SPLASHSCREEN, // کنترل نویگیشن اصلی (BaseNav)
    val authStatus: UiState<Unit> = UiState.Idle, // وضعیت درخواست نتورک (لاگین)
    val isSignUpMode: Boolean = false,            // وضعیت نمایش فرم (ورود یا ثبت‌نام)
    val toastMessage: String? = null              // متنی که روی نال هست برای توست‌ها
)