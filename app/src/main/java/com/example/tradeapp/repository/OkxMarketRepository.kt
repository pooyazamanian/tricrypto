package com.example.tradeapp.repository

import android.util.Log
import com.example.tradeapp.dto.CryptoTicker
import com.example.tradeapp.damin.repository.MarketRepository
import com.example.tradeapp.dto.OkxTickerSocketResponse
import kotlinx.coroutines.flow.SharedFlow
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OkxMarketRepository @Inject constructor(
    private val client: HttpClient
) : MarketRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var session: DefaultClientWebSocketSession? = null
    private var socketJob: Job? = null

    private val cache = mutableMapOf<String, CryptoTicker>()

    private val _tickerStream =
        MutableSharedFlow<Map<String, CryptoTicker>>(replay = 1)

    override val tickerStream =
        _tickerStream.asSharedFlow()

    // 🔥 GLOBAL SYMBOL SET
    private val symbols = mutableSetOf<String>()

    // prevent spam reconnect
    private var lastSentSymbols = emptySet<String>()

    override suspend fun connect(
        symbols: List<String>
    ) {
        val incoming = symbols.toSet()
        val before = this.symbols.toSet()

        // فقط اضافه کن
        this.symbols.addAll(incoming)

        val after = this.symbols.toSet()
        // هیچ چیز جدیدی اضافه نشده
        if (before == after) return

        reconnect()
    }

    private fun reconnect() {

        socketJob?.cancel()

        socketJob = scope.launch {

            while (isActive) {

                try {
                    session?.close()
                    session = client.webSocketSession {
                        url {
                            protocol = URLProtocol.WSS
                            host = "ws.okx.com"
                            port = 8443
                            path(
                                "/ws/v5/public"
                            )
                        }
                    }

                    subscribe(symbols.toList())

                    val emitJob = launch {
                        while (isActive) {
                            _tickerStream.emit(cache.toMap())
                            delay(2000)
                        }
                    }

                    for (frame in session!!.incoming) {

                        if (frame !is Frame.Text) continue

                        handle(frame.readText())
                    }

                    emitJob.cancel()

                } catch (e: Exception) {
                    delay(3000) // auto reconnect
                }
            }
        }
    }

    private suspend fun subscribe(list: List<String>) {
        val args = list.joinToString(",") {
            """{"channel":"tickers","instId":"$it"}"""
        }
        Log.d(
            "WS",
            "📡 subscribe sent ${"""
                {
                  "op":"subscribe",
                  "args":[$args]
                }
                """.trimIndent()}"
        )
        session?.send(
            Frame.Text(
                """{"op":"subscribe","args":[$args]}"""
            )
        )
    }

    private fun handle(text: String) {

        val res =
            json.decodeFromString<OkxTickerSocketResponse>(text)

        val t = res.data.firstOrNull() ?: return

        val last = t.last.toDouble()
        val open = t.open24h.toDouble()

        cache[t.instId] = CryptoTicker(
            symbol = t.instId,
            price = last,
            percentChange = ((last - open) / open) * 100
        )
    }

    override suspend fun disconnect() {
        socketJob?.cancel()
        session?.close()
        cache.clear()
        symbols.clear()
    }
}





