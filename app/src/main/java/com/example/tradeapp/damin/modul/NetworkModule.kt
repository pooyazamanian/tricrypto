package com.example.tradeapp.damin.modul

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val NETWORK_TIMEOUT = 60_000L // 60 ثانیه

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {

            // 🔹 JSON config
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        useAlternativeNames = false
                        encodeDefaults = true
                    }
                )
            }

            // 🔹 Timeout config
            install(HttpTimeout) {
                requestTimeoutMillis = NETWORK_TIMEOUT
                connectTimeoutMillis = NETWORK_TIMEOUT
                socketTimeoutMillis = NETWORK_TIMEOUT
            }

            // 🔹 Logging
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("Logger Ktor =>", message)
                    }
                }
                level = LogLevel.INFO // 🔸 ALL گاهی باعث delay یا crash میشه
            }

            // 🔹 Response observer
            install(ResponseObserver) {
                onResponse { response ->
                    Log.d("HTTP status:", "${response.status.value}")
                }
            }

            // 🔹 Default headers
            install(DefaultRequest) {
                header(HttpHeaders.Accept, ContentType.Application.Json)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            // 🔹 Extra: handle chunked/invalid responses safely
            engine {
                // اگه chunk ناقص بیاد، Ktor قطعش نکنه، تا بتونه دوباره بخونه
                endpoint {
                    keepAliveTime = 5000
                    connectAttempts = 5
                    connectTimeout = NETWORK_TIMEOUT.toInt().toLong()
                }

                // به سرور اجازه بده بسته رو دوباره بفرسته
                requestTimeout = NETWORK_TIMEOUT
            }

            // 🔹 Optional: automatic retry for unstable servers
            install(HttpRequestRetry) {
                retryOnException(maxRetries = 3)
                exponentialDelay()
            }
        }
    }
}
