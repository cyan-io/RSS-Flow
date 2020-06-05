package com.sun.kikyorss.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.sun.kikyorss.*
import com.sun.kikyorss.database.*
import com.sun.kikyorss.message.MsgType
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader

class MainActivity : AppCompatActivity() {

    lateinit var myViewModel: MyViewModel
    lateinit var toolBar: androidx.appcompat.widget.Toolbar
    val msg=MyApplication.msg
    var hasNotRefresh = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init_webView.loadData(null, null, null)
        supportFragmentManager.beginTransaction().add(R.id.frag_container, ChannelFragment())
            .commit()
        toolBar = toolbar
        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        checkUpdate(frag_container, this)
    }

    fun getChannel(url: String) {
        val urlFormated= formatUrl(url)
        if(MyApplication.channelDao.isExist(formatUrl(urlFormated))){
            msg.sendMsg(MsgType.ERROR_EXIST)
            return
        }
        val client = MyOkHttp.getClient()
        val request = MyOkHttp.getRequset(urlFormated)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                msg.sendMsg(MsgType.ERROR_NET)
            }

            override fun onResponse(call: Call, response: Response) {
                val response_body = response.body?.string().toString()
                val isrss2 = isRss2(response_body)
                if (isrss2)
                    try {
                        Log.i("__isRss2", "ture")
                        var title: String = ""
                        var description: String = ""
                        val parser = XmlPullParserFactory.newInstance().newPullParser()
                        parser.setInput(StringReader(response_body))
                        var eventType = parser.eventType

                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (parser.depth == 3 && eventType == XmlPullParser.START_TAG)
                                when (parser.name) {
                                    "title" -> title = parser.nextText()
                                    "description" -> description = parser.nextText()
                                }
                            eventType = parser.next()
                        }
                        MyApplication.channelDao.insert(Channel(title, urlFormated, description))
                        msg.sendMsg(MsgType.SUCCESS_ADD)
                    } catch (e: Exception) {
                        msg.sendMsg(MsgType.ERROR)
                    }
                else
                    msg.sendMsg(MsgType.ERROR_WRONG_FEED)
            }
        })
    }
}

