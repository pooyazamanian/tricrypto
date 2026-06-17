package com.example.tradeapp.repository.util

import com.example.tradeapp.viewmodel.TimeRange

fun TimeRange.toLimit(): Int =
    when (this) {

        TimeRange.ONE_DAY_BEFORE -> 288
        TimeRange.ONE_DAY -> 288

        TimeRange.FIVE_DAYS -> 480

        TimeRange.ONE_MONTH -> 720

        TimeRange.FIVE_MONTHS -> 150

        TimeRange.ONE_YEAR -> 365

        TimeRange.ALL -> 300
    }