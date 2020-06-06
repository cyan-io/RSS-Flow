package com.sun.kikyorss

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.sun.kikyorss.database.*

class MyApplication : Application() {
    companion object {
        lateinit var context: Context
        lateinit var itemDao: ItemDao
        lateinit var channelDao: ChannelDao
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        itemDao = ItemDatabase.getDatabase(this).itemDao()
        channelDao = ChannelDatabase.getDatabase(this).channelDao()
    }
}