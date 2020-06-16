package com.sun.kikyorss.logic

import android.app.Application
import android.content.Context
import com.sun.kikyorss.database.*

class MyApplication : Application() {
    companion object {
        lateinit var appContext: Context
        lateinit var itemDao: ItemDao
        lateinit var channelDao: ChannelDao
        const val releasePage="http://kikyo.ink/index.php/rssflow.html"
        const val versionUrl="https://maki863.gitee.io/kikyorss/version.json"
        const val currentVersion=1
        const val netTimeout=10L
        const val javaScriptEnabled=false
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        itemDao = ItemDatabase.getDatabase(this).getItemDao()
        channelDao = ChannelDatabase.getDatabase(this).getChannelDao()
    }
}