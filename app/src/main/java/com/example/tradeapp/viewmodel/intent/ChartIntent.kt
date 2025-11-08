package com.example.tradeapp.viewmodel.intent

import com.example.tradeapp.viewmodel.TimeRange

sealed class ChartIntent {
    data class LoadChartData(
        val symbol: String,
        val timeRange: TimeRange
    ) : ChartIntent()

    data class LoadAssetInfo(val assetId: String) : ChartIntent()
    data class SelectTimeRange(val timeRange: TimeRange) : ChartIntent()
    object RefreshData : ChartIntent()
}
