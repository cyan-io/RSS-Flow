package com.sun.kikyorss.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.sun.kikyorss.R
import com.sun.kikyorss.database.Item
import com.sun.kikyorss.logic.MyApplication
import com.sun.kikyorss.logic.resizeHtmlImg
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_article.view.*
import kotlin.concurrent.thread


class ArticleFragment(private val item: Item) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.toolBar.apply {
            menu.findItem(R.id.add_feed).isVisible = false
            title = item.title
        }

        var dx:Int=0

        val fab=browser

        article_detail.apply {
            settings.javaScriptEnabled = MyApplication.javaScriptEnabled
            loadDataWithBaseURL(
                item.link,
                resizeHtmlImg(item.description),
                null,
                null,
                null
            )
            setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                Log.i("onScrool","$dx")
                dx+=scrollY-oldScrollY
                if(dx>200){
                    dx=0
                    try {
                        fab.hide()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                if(dx<-100){
                    dx=0
                    try{
                        fab.show()
                    }catch (e:Exception){}
                }
            }
        }

        browser.setOnClickListener {
            val uri: Uri = Uri.parse(item.link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

}
