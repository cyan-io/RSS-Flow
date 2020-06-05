package com.sun.kikyorss

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sun.kikyorss.database.*

class MyViewModel : ViewModel() {
    val itemList: MutableList<Item>
    val channelList: MutableList<Channel>
    val itemMap: MutableMap<Channel, MutableList<Item>>
    val onFreshOver=MutableLiveData<Boolean>()
    val channelDao=ChannelDatabase.getDatabase(MyApplication.context).channelDao()
    val itemDao=ItemDatabase.getDatabase(MyApplication.context).itemDao()

    init {

        itemList = mutableListOf()
        channelList = mutableListOf()
        itemMap = mutableMapOf()
    }

    fun refresh() {
        itemList.clear()
        itemList.addAll(itemDao.loadAll())
        channelList.clear()
        channelList.addAll(channelDao.loadAll())
        itemMap.clear()
        for (i in channelList) {
            itemMap[i] = itemDao.load(i.link).toMutableList()
        }
        onFreshOver.value=true
    }
}