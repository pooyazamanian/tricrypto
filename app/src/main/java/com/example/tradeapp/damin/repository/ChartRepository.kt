package com.example.tradeapp.damin.repository

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.tradeapp.damin.model.HistoryData
import com.example.tradeapp.damin.model.MarketData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChartRepository @Inject constructor(
    private val client: HttpClient
) {
//    private val _messages = MutableStateFlow<List<MarketData>>(emptyList())
//    val messages: StateFlow<List<MarketData>> = _messages.asStateFlow()


//    private val _historyData = MutableStateFlow<List<HistoryData>>(emptyList())
//    val historyData: StateFlow<List<HistoryData>> = _historyData.asStateFlow()


    val state = mutableStateOf(true)
    suspend fun startWebSocket() = flow {
        state.value = true
        while (state.value) {
            try {
                client.webSocket("wss://stream.tgju.org/connection/websocket") {
                    // ارسال پیام‌های اولیه
                    send(Frame.Text("""{"params":{"name":"js"},"id":1}"""))
                    // منتظر پاسخ اولیه
                    val firstResponse = incoming.receive() as? Frame.Text
                    if (firstResponse != null) {
                        send(Frame.Text("""{"method":1,"params":{"channel":"tgju:stream"},"id":2}"""))
                    }
                    // دریافت پیام‌ها
                    while (isActive) {
                        val frame = incoming.receive()
                        if (frame is Frame.Text) {
                            val message = frame.readText()
                            parseAndInsert(message)
                        }
                    }
                }
            } catch (e: Exception) {
                emit( MarketData(
                    cryptoId = "error",
                    cryptoName = "error",
                    livePrice = null,
                    maxPrice = null,
                    minPrice = null,
                    avgPrice = null,
                    volume = null,
                    rate = null,
                    state = "error",
                    timestamp = java.time.LocalDateTime.now().toString()
                )
                )
                delay(5000) // تأخیر 5 ثانیه قبل از اتصال مجدد
            }
        }
    }

    private suspend fun FlowCollector<MarketData>.parseAndInsert(
        message: String
    ) {
        try {
            val json = Json.parseToJsonElement(message).jsonObject
            val dataArray =
                json["result"]?.jsonObject?.get("data")?.jsonObject?.get("data")?.jsonArray ?: return

            for (data in dataArray) {
                val marketDataString = data.jsonPrimitive.content
                val parts = marketDataString.split("|")
                if (parts.size >= 16 && parts[0] == "market") {
                    val marketData = MarketData(
                        cryptoId = parts[1],
                        cryptoName = parts[2],
                        livePrice = parts.getOrNull(7)?.toDoubleOrNull(),
                        maxPrice = parts.getOrNull(8)?.toDoubleOrNull(),
                        minPrice = parts.getOrNull(9)?.toDoubleOrNull(),
                        avgPrice = parts.getOrNull(10)?.toDoubleOrNull(),
                        volume = parts.getOrNull(11)?.toDoubleOrNull(),
                        rate = parts.getOrNull(12)?.toDoubleOrNull(),
                        state = parts[13],
                        timestamp = parts[15]
                    )
                    emit(marketData)
                }
            }
        } catch (e: Exception) {
            emit(
                MarketData(
                    cryptoId = "parse_error",
                    cryptoName = "parse_error",
                    livePrice = null,
                    maxPrice = null,
                    minPrice = null,
                    avgPrice = null,
                    volume = null,
                    rate = null,
                    state = "error",
                    timestamp = LocalDateTime.now().toString()
                )
            )
        }
    }

    suspend fun fetchHistory(symbol: String, resolution: String, from: Long, to: Long)  = flow {
        try {
            val response: HistoryData = client.get("https://dashboard-api.tgju.org/v1/tv2/history") {
                parameter("symbol", symbol)
                parameter("resolution", resolution)
                parameter("from", from)
                parameter("to", to)
            }.body()

            emit(response)
//            _historyData.value = _historyData.value + response

        } catch (e: Exception) {
            Log.e("error in fetchHistory" , e.message.toString())
            emit(HistoryData(
                o = emptyList(),
                c = emptyList(),
                h = emptyList(),
                l = emptyList(),
                t = emptyList(),
                v = emptyList(),
                s = e.message.toString()
            ))
        }
    }


    fun closeWebSocket() {
        state.value = false
        client.close()
    }

}