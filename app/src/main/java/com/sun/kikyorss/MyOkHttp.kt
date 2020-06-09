package com.sun.kikyorss

import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import java.util.concurrent.TimeUnit

object MyOkHttp {
    fun getClient(): OkHttpClient {
        return OkHttpClient.Builder().connectionSpecs(listOf(ConnectionSpec.COMPATIBLE_TLS))
            .connectTimeout(Configurations.netTimeout, TimeUnit.SECONDS).build()
        /*.readTimeout(Configurations.netTimeout, TimeUnit.SECONDS)
            .writeTimeout(Configurations.netTimeout, TimeUnit.SECONDS)*/
    }

    fun getRequset(url: String): Request {
        return Request.Builder().apply {
            removeHeader("User-Agent")
            addHeader("User-Agent", Configurations.userAgent)
            url(url)
        }.build()
    }
}