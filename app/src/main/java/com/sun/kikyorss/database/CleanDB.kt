package com.sun.kikyorss.database

import com.sun.kikyorss.MyApplication

class CleanDB {
    private val itemDao = ItemDatabase.getDatabase(MyApplication.context).itemDao()
    private val channelDao = ChannelDatabase.getDatabase(MyApplication.context).channelDao()
    private val channelList = channelDao.loadAll()

    init {
        for(i in itemDao.loadAll()){
            if(channelList.any{channel ->i.parent!=channel.link})
                itemDao.delete(i)
        }
    }
}