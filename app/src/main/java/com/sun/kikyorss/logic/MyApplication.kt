package com.sun.kikyorss.logic

import android.app.Application
import android.content.Context
import com.sun.kikyorss.database.*

class MyApplication : Application() {
    companion object {
        lateinit var context: Context
        lateinit var itemDao: ItemDao
        lateinit var channelDao: ChannelDao
        const val releasePage="http://kikyo.ink/index.php/rssflow.html"
        const val versionUrl="https://maki863.gitee.io/kikyorss/version.json"
        const val currentVersion=1
        const val netTimeout=10L
        const val javaScriptEnabled=false
        const val userAgent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, " +
                "like Gecko) Chrome/83.0.4103.61 Safari/537.36 Edg/83.0.478.37"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        itemDao = ItemDatabase.getDatabase(this).getItemDao()
        channelDao = ChannelDatabase.getDatabase(this).getChannelDao()
    }
}