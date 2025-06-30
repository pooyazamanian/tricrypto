package com.example.tradeapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tradeapp.damin.repository.ChartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ChartViewModel @Inject constructor(
    private val chartRepository: ChartRepository
) : ViewModel() {

    suspend fun fetchChartData()  = chartRepository.fetchHistory(symbol = "crypto-bitcoin", resolution = "5", from = 1750701628,to = 1751133688)

}