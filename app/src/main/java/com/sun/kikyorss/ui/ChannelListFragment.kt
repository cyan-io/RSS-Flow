package com.sun.kikyorss.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.sun.kikyorss.*
import com.sun.kikyorss.logic.MyApplication.Companion.channelDao
import com.sun.kikyorss.logic.MyApplication.Companion.itemDao
import com.sun.kikyorss.logic.*
import com.sun.kikyorss.ui.MainActivity.Companion.mainActivityContext

import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader

class ChannelListFragment : Fragment() {
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = activity as MainActivity
        val fragmentManager = MainActivity.mainFragmentManager
        val channelList = channelDao.loadAll()
        val channelAdapter = ChannelListAdapter(channelList)

        MainActivity.toolBar.apply {
            title = "订阅列表"
            menu.findItem(R.id.add_feed).isVisible = true
            setOnMenuItemClickListener { item ->
                if (item != null)
                    when (item.itemId) {
                        R.id.add_feed -> {
                            MaterialDialog(mainActivityContext).show {
                                title(text = "添加订阅，仅支持RSS 2.0")
                                input(hint= "http(s)://") { _, text ->
                                    var right = false
                                    try {
                                        Request.Builder().url(text.toString()).build()
                                        right = true
                                    } catch (e: java.lang.Exception) {
                                        Toasty.error(
                                            MyApplication.appContext,
                                            "错误的网址",
                                            Toasty.LENGTH_LONG
                                        ).show()
                                    }
                                    if (right)
                                        getChannel(text.toString())
                                }
                                positiveButton(text = "添加")
                                negativeButton(text = "取消")
                            }
                        }
                        R.id.info_frag -> {
                            if(!(fragmentManager.findFragmentById(R.id.frag_container) is InfoFragment)){
                                fragmentManager.beginTransaction()
                                    .replace(R.id.frag_container, InfoFragment())
                                    .addToBackStack(null)
                                    .commit()
                            }
                        }
                    }
                true
            }
        }

        val swipeRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            if (channelDao.size() > 0) {
                swipeRefresh.isRefreshing = true
                channelList.clear()
                channelList.addAll(channelDao.loadAll())
                channelAdapter.notifyDataSetChanged()

                var success = 0
                var fail = 0
                val l = LoadItemUnit()
                l.onResponse.observe(mainActivity, Observer {
                    if (it.size == channelList.size) {
                        for (i in it) {
                            if (i.second) success += 1 else fail += 1
                        }
                        if (fragmentManager.findFragmentById(R.id.frag_container) is ChannelListFragment) {
                            swipeRefresh.isRefreshing = false
                        }
                        Toasty.info(
                            MyApplication.appContext,
                            "${success}成功 ${fail}失败",
                            Toasty.LENGTH_SHORT
                        ).show()
                    }
                })
            } else
                swipeRefresh.isRefreshing = false
        }

        swipeRefresh.setOnRefreshListener(swipeRefreshListener)

        if (MainActivity.hasNotRefresh && channelDao.size() > 0) {
            swipeRefreshListener.onRefresh()
            MainActivity.hasNotRefresh = false
        }

        feedsRecycleView.apply {
            layoutManager = LinearLayoutManager(MyApplication.appContext)
            adapter = channelAdapter
        }

        channelAdapter.setOnItemClickListener(object : ChannelListAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                fragmentManager.beginTransaction()
                    .replace(R.id.frag_container, ItemListFragment(channelList[position]))
                    .addToBackStack(null)
                    .commit()
            }

            override fun onLongClick(view: View, position: Int) {
                MaterialDialog(mainActivityContext).show {
                    title(text = channelList[position].title)
                    message(text = channelList[position].description)
                    positiveButton(text = "删除") {
                        channelDao.delete(channelList[position])
                        itemDao.deleteByParent(channelList[position].url)
                        Toasty.success(
                            MyApplication.appContext,
                            "已删除 " + channelList[position].title,
                            Toasty.LENGTH_SHORT
                        ).show()
                        channelList.removeAt(position)
                        channelAdapter.notifyDataSetChanged()
                    }
                    negativeButton(text = "取消")
                }
            }
        })
    }

    private fun getChannel(url: String) {
        if (channelDao.isExist(url)) {
            Toasty.error(MyApplication.appContext, "订阅已存在", Toasty.LENGTH_SHORT).show()
            return
        }
        val client = MyOkHttp.getClient()
        val request = MyOkHttp.getRequest(url)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                Toasty.error(MyApplication.appContext, "网络或网址错误", Toasty.LENGTH_LONG).show()
                Looper.loop()
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string().toString()
                val isRss = isRss2(responseBody)
                Log.i("__resp", responseBody)
                if (isRss)
                    try {
                        var title: String = ""
                        var description: String = ""
                        var link: String = ""
                        var pubDate:String?=null
                        var lastBuildDate:String?=null

                        val parser = XmlPullParserFactory.newInstance().newPullParser()
                        parser.setInput(StringReader(responseBody))
                        var eventType = parser.eventType

                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (parser.depth == 3 && eventType == XmlPullParser.START_TAG)
                                when (parser.name) {
                                    "title" -> title = parser.nextText()
                                    "description" -> description = parser.nextText()
                                    "link" -> link = parser.nextText()
                                    "pubDate"->pubDate=parser.nextText()
                                    "lastBuildDate"->lastBuildDate=parser.nextText()
                                }
                            eventType = parser.next()
                        }
                        channelDao.insert(
                            com.sun.kikyorss.database.Channel(
                                url,
                                title,
                                link,
                                description,
                                pubDate,
                                lastBuildDate
                            )
                        )
                        Looper.prepare()
                        Toasty.success(MyApplication.appContext, "添加成功,请手动刷新", Toasty.LENGTH_SHORT)
                            .show()
                        Looper.loop()
                    } catch (e: Exception) {
                        Looper.prepare()
                        Toasty.error(MyApplication.appContext, "请检查Xml格式", Toasty.LENGTH_LONG)
                            .show()
                        Looper.loop()
                    }
                else {
                    Looper.prepare()
                    Toasty.error(
                        MyApplication.appContext,
                        "错误的订阅地址",
                        Toasty.LENGTH_LONG
                    ).show()
                    Looper.loop()
                }
            }
        })
    }
}
