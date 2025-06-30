package com.example.tradeapp.ui.theme

data class BottomNavigationItem(
    val title: String,
    val unSelectedIcon: Int?,
    val SelectedIcon: Int?,
    val rout: String,
    val onClick: () -> Unit = {}
)