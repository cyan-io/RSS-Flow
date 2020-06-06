package com.sun.kikyorss

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.sun.kikyorss.database.*
import com.sun.kikyorss.message.Msg
import com.sun.kikyorss.message.Msg2

class MyApplication : Application() {
    companion object {
        lateinit var context: Context
        lateinit var itemDao: ItemDao
        lateinit var channelDao: ChannelDao
        lateinit var msg: Msg2
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        itemDao = ItemDatabase.getDatabase(this).itemDao()
        channelDao = ChannelDatabase.getDatabase(this).channelDao()
        msg = Msg2()
    }
}