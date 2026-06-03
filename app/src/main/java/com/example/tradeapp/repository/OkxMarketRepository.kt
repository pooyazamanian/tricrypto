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
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
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

    override suspend fun connect(symbols: List<String>) {

        val newSymbols = symbols.toSet()

        Log.d("WS", "🚀 connect() called with: $newSymbols")

        if (newSymbols == lastSentSymbols) {
            Log.d("WS", "⏭ same symbols, skip reconnect")
            return
        }

        lastSentSymbols = newSymbols
        this.symbols.clear()
        this.symbols.addAll(newSymbols)

        Log.d("WS", "🔁 symbols updated -> reconnect()")

//        reconnect()
    }


    private fun reconnect() {

        Log.d("WS", "♻️ reconnect() triggered")

        socketJob?.cancel()
        Log.d("WS", "🧹 previous socketJob cancelled")

        socketJob = scope.launch {

            Log.d("WS", "🟡 socketJob started")

            while (isActive) {

                try {
                    Log.d("WS", "🌐 creating websocket session...")

                    session?.close()
                    Log.d("WS", "🔌 old session closed")

                    session = client.webSocketSession {
                        url {
                            protocol = URLProtocol.WSS
                            host = "ws.okx.com"
                            port = 8443
                            path("/ws/v5/public")
                        }
                    }

                    Log.d("WS", "✅ websocket session created")

                    Log.d("WS", "📡 subscribing to: $symbols")
                    subscribe(symbols.toList())
                    Log.d("WS", "📡 subscribe sent")

                    val emitJob = launch {

                        Log.d("WS", "📤 emitJob started")

                        while (isActive) {
                            Log.d("WS", "📤 emitting cache size=${cache.size}")

                            _tickerStream.emit(cache.toMap())

                            delay(2000)
                        }
                    }

                    Log.d("WS", "👂 entering incoming loop")

                    for (frame in session!!.incoming) {

                        Log.d("WS", "📩 frame received: ${frame.frameType}")

                        if (frame !is Frame.Text) {
                            Log.d("WS", "⚠️ non-text frame ignored")
                            continue
                        }

                        val text = frame.readText()

                        Log.d("WS", "📩 raw message: $text")

                        handle(text)
                    }

                    Log.d("WS", "❌ incoming loop exited")
                    emitJob.cancel()

                } catch (e: Exception) {

                    Log.e("WS", "💥 websocket error: ${e.message}", e)

                    delay(3000)
                    Log.d("WS", "🔄 retrying reconnect...")
                }
            }
        }
    }
    private suspend fun subscribe(list: List<String>) {
        val args = list.joinToString(",") {
            """{"channel":"tickers","instId":"$it"}"""
        }

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
            percentChange = ((last - open) / open) * 100,
//            high24h = t.high24h.toDouble(),
//            low24h = t.low24h.toDouble()
        )
    }

    override suspend fun disconnect() {
        socketJob?.cancel()
        session?.close()
        cache.clear()
        symbols.clear()
    }
}




