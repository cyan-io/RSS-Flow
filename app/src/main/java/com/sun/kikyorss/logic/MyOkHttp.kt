package com.sun.kikyorss.logic

import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object MyOkHttp {

    const val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, " +
            "like Gecko) Chrome/83.0.4103.61 Safari/537.36 Edg/83.0.478.37"

    fun getClient(): OkHttpClient {
        return OkHttpClient.Builder().connectionSpecs(
            listOf(
                ConnectionSpec.COMPATIBLE_TLS,
                ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS, ConnectionSpec.RESTRICTED_TLS
            )
        ).connectTimeout(MyApplication.netTimeout, TimeUnit.SECONDS).build()
    }

    fun getRequest(url: String): Request {
        return Request.Builder().apply {
            addHeader("User-Agent", userAgent)
            url(url)
        }.build()
    }
}