package com.sun.kikyorss

import androidx.lifecycle.MutableLiveData
import com.sun.kikyorss.database.*
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableEmitter
import io.reactivex.rxjava3.functions.Consumer
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader
import java.lang.Exception

class LoadItemUnit2(var channelList: List<Channel>?) {

    private val itemDao = MyApplication.itemDao
    private val channelDao = MyApplication.channelDao
    private lateinit var emitter: FlowableEmitter<Pair<String,Boolean>>
    val onResponse=MutableLiveData<MutableList<Pair<String,Boolean>>>()
    private val mutableList= mutableListOf<Pair<String,Boolean>>()


    init {
        Flowable.create<Pair<String,Boolean>>({emitter ->
            this.emitter=emitter
        },BackpressureStrategy.BUFFER).subscribe(Consumer {
            if(it!=null)
                mutableList.add(it)
            onResponse.postValue(mutableList)
        })


        if (channelList == null)
            channelList = channelDao.loadAll()

        if (channelList!!.isNotEmpty()) {
            val client = MyOkHttp.getClient()

            for (i in channelList!!) {
                loadItem(client, i.link)
            }
        }
    }

    private fun loadItem(client: OkHttpClient, url: String) {
        val request = MyOkHttp.getRequset(url)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //resPostValue(url, false)
                emitter.onNext(Pair(url,false))
            }

            override fun onResponse(call: Call, response: Response) {
                prase(response.body?.string().toString(), url)
            }
        })
    }

    private fun prase(content: String, parent: String) {
        val listItem = mutableListOf<Item>()
        var signal=false
        try {
            var title: String = ""
            var link: String = ""
            var description: String = ""
            var pubDate: String = ""

            val parser = XmlPullParserFactory.newInstance().newPullParser()
            parser.setInput(StringReader(content))
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.depth > 3)
                    when (parser.name) {
                        "title" -> title = parser.nextText()
                        "link" -> link = parser.nextText()
                        "description" -> description = parser.nextText()
                        "content:encoded" -> description = parser.nextText()
                        "pubDate" -> pubDate = parser.nextText()
                    }
                if (eventType == XmlPullParser.END_TAG && parser.name == "item") {
                    val item = Item(parent, title, link, description, pubDate)
                    listItem.add(item)
                    title = ""
                    link = ""
                    description = ""
                    pubDate = ""
                }
                eventType = parser.next()
            }
            signal = true
        } catch (e: Exception) {
            signal = false
        }
        emitter.onNext(Pair(parent,signal))
        itemDao.compareInsertDelete(listItem)
    }
}