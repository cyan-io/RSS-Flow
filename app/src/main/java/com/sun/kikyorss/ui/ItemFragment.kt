package com.sun.kikyorss.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.sun.kikyorss.R
import com.sun.kikyorss.database.Channel
import com.sun.kikyorss.database.Item
import com.sun.kikyorss.database.ItemDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_article_list.*

class ItemFragment(val channel: Channel) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        val vm = mainActivity.myViewModel
        mainActivity.toolBar.menu.findItem(R.id.add_feed).isVisible = false
        mainActivity.toolBar.title = channel.title

        val itemAdapter =
            mainActivity.myViewModel.itemMap[channel]?.let { ItemAdapter(it, mainActivity.supportFragmentManager) }

        articlesRecycleView.layoutManager = LinearLayoutManager(activity)
        articlesRecycleView.adapter = itemAdapter
    }
}
