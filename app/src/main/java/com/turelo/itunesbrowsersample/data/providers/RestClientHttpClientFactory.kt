package com.turelo.itunesbrowsersample.data.providers

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class RestClientHttpClientFactory {
    companion object {

        private const val READ_TIMEOUT_MS = 3L
        private const val WRITE_TIMEOUT_MS = 3L

        fun createHttpClient(): OkHttpClient {
            return OkHttpClient().newBuilder()
                .connectTimeout(RestProvider.CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT_MS, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_MS, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor(
                ).apply {
                    this.setLevel(HttpLoggingInterceptor.Level.BASIC)
                })
                .build()
        }
    }
}