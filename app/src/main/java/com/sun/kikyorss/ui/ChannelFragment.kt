package com.sun.kikyorss.ui

import android.os.Bundle
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
import com.sun.kikyorss.MyApplication.Companion.channelDao
import com.sun.kikyorss.MyApplication.Companion.itemDao

import com.sun.kikyorss.message.MsgType
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_main.*
import okhttp3.*

class ChannelFragment : Fragment() {
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
        val viewModel = mainActivity.myViewModel
        val fragmentManager = mainActivity.supportFragmentManager
        val channelList = viewModel.channelList
        val channelAdapter = ChannelAdapter(viewModel.channelList)

        mainActivity.toolBar.title = "订阅列表"


        val addFeed = mainActivity.toolBar.menu.findItem(R.id.add_feed)
        addFeed.isVisible = true

        mainActivity.toolBar.setOnMenuItemClickListener { item ->
            if (item != null)
                when (item.itemId) {
                    R.id.add_feed -> {
                        MaterialDialog(mainActivity).show {
                            title(text = "添加订阅")
                            message(text = "仅支持RSS 2.0 协议")
                            input { dialog, text ->
                                var right = false
                                try {
                                    Request.Builder().url(text.toString()).build()
                                    right = true
                                } catch (e: java.lang.Exception) {
                                    MyApplication.msg.sendMsg(MsgType.ERROR_WRONG_FEED)
                                }
                                if (right)
                                    mainActivity.getChannel(text.toString())
                            }
                            positiveButton(text = "添加")
                            negativeButton(text = "取消")
                        }
                    }
                    R.id.info_frag -> {
                        if (!(fragmentManager.fragments.last() is InfoFragment))
                            fragmentManager.beginTransaction()
                                .replace(R.id.frag_container, InfoFragment())
                                .addToBackStack(null)
                                .commit()
                    }
                }
            true
        }

        val swipeRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            swipeRefresh.isRefreshing = true
            viewModel.channelList.clear()
            viewModel.channelList.addAll(MyApplication.channelDao.loadAll().toMutableList())
            channelAdapter.notifyDataSetChanged()

            var success = 0
            var fail = 0
            val l = LoadItemUnit2(null)
            l.onResponse.observe(mainActivity, Observer {
                if (it.size == channelList.size) {
                    for (i in it) {
                        if (i.second) success += 1 else fail += 1
                    }
                    if (fragmentManager.findFragmentById(R.id.frag_container) is ChannelFragment) {
                        swipeRefresh.isRefreshing = false
                    }
                    mainActivity.myViewModel.refresh()
                    Toasty.info(mainActivity, "${success}成功 ${fail}失败", Toasty.LENGTH_SHORT).show()
                }
            })
        }

        swipeRefresh.setOnRefreshListener(swipeRefreshListener)

        if (mainActivity.hasNotRefresh) {
            swipeRefreshListener.onRefresh()
            mainActivity.hasNotRefresh = false
        }

        feedsRecycleView.apply {
            layoutManager = LinearLayoutManager(mainActivity)
            adapter = channelAdapter
        }

        channelAdapter.setOnItemClickListener(object : ChannelAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                fragmentManager.beginTransaction()
                    .replace(R.id.frag_container, ItemFragment(channelList[position]))
                    .addToBackStack(null)
                    .commit()
            }

            override fun onLongClick(view: View, position: Int) {
                MaterialDialog(mainActivity).show {
                    title(text = channelList[position].title)
                    message(text = channelList[position].description)
                    input(hint = "输入新的标题") { materialDialog, charSequence ->
                        channelList[position].title = charSequence.toString()
                        channelDao.update(channelList[position])
                        channelList.clear()
                        channelList.addAll(channelDao.loadAll().toMutableList())
                        channelAdapter.notifyDataSetChanged()
                    }
                    positiveButton(text = "修改标题")
                    negativeButton(text = "删除订阅") {
                        channelDao.delete(channelList[position])
                        itemDao.deleteByChannel(channelList[position].link)
                        Toasty.success(
                            mainActivity,
                            "已删除 " + channelList[position].title,
                            Toasty.LENGTH_SHORT
                        ).show()
                        channelList.removeAt(position)
                        channelAdapter.notifyDataSetChanged()
                    }
                    neutralButton(text = "取消")
                }
            }
        })
    }
}
