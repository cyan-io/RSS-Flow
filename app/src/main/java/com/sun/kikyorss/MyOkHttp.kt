package com.sun.kikyorss

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object MyOkHttp {
    fun getClient(): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(Configurations.netTimeout, TimeUnit.SECONDS)
            .readTimeout(Configurations.netTimeout, TimeUnit.SECONDS)
            .writeTimeout(Configurations.netTimeout, TimeUnit.SECONDS).build()
    }

    fun getRequset(url: String): Request {
        return Request.Builder().apply {
            removeHeader("User-Agent")
            addHeader("User-Agent", Configurations.userAgent)
            url(url)
        }.build()
    }
}