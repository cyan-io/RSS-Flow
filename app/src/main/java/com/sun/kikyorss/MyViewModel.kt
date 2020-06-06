package com.sun.kikyorss

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sun.kikyorss.MyApplication.Companion.channelDao
import com.sun.kikyorss.MyApplication.Companion.itemDao
import com.sun.kikyorss.database.*

class MyViewModel : ViewModel() {
    val itemMap: MutableMap<Channel, MutableList<Item>> = mutableMapOf()
    val onFreshOver = MutableLiveData<Boolean>()
    val channelList= mutableListOf<Channel>()

    init {
        refresh()
    }

    fun refresh() {
        channelList.clear()
        channelList.addAll(channelDao.loadAll())
        itemMap.clear()
        for (i in channelList)
            itemMap[i] = itemDao.load(i.link).toMutableList()
        onFreshOver.value = true
    }
}