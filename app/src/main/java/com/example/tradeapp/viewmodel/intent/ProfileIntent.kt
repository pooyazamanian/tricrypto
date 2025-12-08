package com.example.tradeapp.viewmodel.intent

sealed class ProfileIntent {
    object LoadProfile : ProfileIntent ()
    object LoadUser : ProfileIntent ()
}