package com.sun.kikyorss

import kotlinx.coroutines.processNextEventInCurrentThread
import okhttp3.Call
import okhttp3.Response
import java.io.IOException
import javax.security.auth.callback.Callback

fun main() {
    val client = MyOkHttp.getClient()
    val request =
        MyOkHttp.getRequset("https://www.tsdm39.net/forum.php?mod=rss&fid=247&auth=5903QG%252FPzBtakmk2f933XqqwHcJq2txomobhss9uqnmXCa9urM%252BWmQqCsV5%252BDJEZXw")
    client.newCall(request).enqueue(object :okhttp3.Callback{
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            print(response.body?.string())
        }
    })
}