package com.example.tradeapp.repository.util

import com.example.tradeapp.viewmodel.TimeRange

fun TimeRange.toOkxBar(): String =
    when (resolution) {
        "5" -> "5m"
        "15" -> "15m"
        "60" -> "1H"
        "D" -> "1D"
        "W" -> "1W"
        else -> "1H"
    }