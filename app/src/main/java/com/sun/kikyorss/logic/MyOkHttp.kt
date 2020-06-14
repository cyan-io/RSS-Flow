package com.sun.kikyorss.logic

import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object MyOkHttp {
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
            addHeader("User-Agent", MyApplication.userAgent)
            url(url)
        }.build()
    }
}