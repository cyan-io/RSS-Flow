package com.sun.kikyorss.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sun.kikyorss.Configurations
import com.sun.kikyorss.R
import com.sun.kikyorss.database.Item
import com.sun.kikyorss.resizeHtmlImg
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_article.*


class ArticleFragment(val item: Item) : Fragment() {

    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity

        mainActivity.toolBar.setTitle(item.title)
        mainActivity.toolBar.menu.findItem(R.id.add_feed).setVisible(false)

        article_detail.settings.javaScriptEnabled = Configurations.javaScriptEnabled

        article_detail.loadDataWithBaseURL(
            item.link,
            resizeHtmlImg(item.description),
            null,
            null,
            null
        )

        browser.setOnClickListener {
            val uri: Uri = Uri.parse(item.link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

}
