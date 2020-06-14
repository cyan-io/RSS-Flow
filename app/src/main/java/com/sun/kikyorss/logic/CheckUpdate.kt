package com.sun.kikyorss.logic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.lang.Exception

class CheckUpdate(view: View, activity: Activity) {
    private data class VersionInfo(val latestVersion: Int)

    private val userAgent =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, " +
                "like Gecko) Chrome/83.0.4103.97 Safari/537.36 Edg/83.0.478.45"
    private lateinit var versionInfo: VersionInfo

    init {
        val client = OkHttpClient.Builder().build()
        val request =
            Request.Builder().url(MyApplication.versionUrl).addHeader("User-Agent", userAgent)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    versionInfo =
                        Gson().fromJson<VersionInfo>(
                            response.body?.string(),
                            VersionInfo::class.java
                        )
                } catch (e: Exception) {
                    Log.i("__update", "gson error")
                    e.printStackTrace()
                } finally {
                    if (::versionInfo.isInitialized)
                        if (versionInfo.latestVersion > MyApplication.currentVersion) {
                            Snackbar.make(view, "新版本已推出，是否下载更新", Snackbar.LENGTH_LONG).setAction(
                                "是"
                            ) {
                                val uri: Uri = Uri.parse(MyApplication.releasePage)
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                activity.startActivity(intent)
                            }.show()
                        }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.i("__update", "onFailure")
            }
        })
    }

}