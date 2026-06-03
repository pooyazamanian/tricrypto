package com.example.tradeapp.usecase

import com.example.tradeapp.damin.repository.MarketRepository
import com.example.tradeapp.dto.CryptoTicker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ObserveMarketBySymbolsUseCase @Inject constructor(
    private val repository: MarketRepository
) {
    operator fun invoke(
        symbols: List<String>
    ): Flow<List<CryptoTicker>> {
        return repository.tickerStream.onStart { repository.connect(symbols) }
            .map { map ->

                symbols.mapNotNull { symbol ->
                    map[symbol]
                }
            }
    }
}
