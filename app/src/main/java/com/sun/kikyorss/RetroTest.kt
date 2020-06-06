package com.sun.kikyorss

import okhttp3.Response
import retrofit2.http.GET

interface RetroTest {
    @GET("http://kikyo.ink")
    fun getPage():Response
}