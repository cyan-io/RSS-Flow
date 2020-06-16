package com.sun.kikyorss.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.sun.kikyorss.R
import com.sun.kikyorss.database.Channel
import com.sun.kikyorss.logic.MyApplication.Companion.itemDao
import kotlinx.android.synthetic.main.fragment_article_list.*
class ItemListFragment(private val channel: Channel) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.toolBar.apply {
            menu.findItem(R.id.add_feed).isVisible = false
            title = channel.title
        }

        val listItem = itemDao.loadByParent(channel.url)
        val itemListAdapter = ItemListAdapter(listItem, MainActivity.mainFragmentManager)

        articlesRecycleView.layoutManager = LinearLayoutManager(activity)
        articlesRecycleView.adapter = itemListAdapter
    }
}
