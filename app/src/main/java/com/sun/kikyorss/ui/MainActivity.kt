package com.sun.kikyorss.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.sun.kikyorss.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var myViewModel: MyViewModel
    lateinit var toolBar: androidx.appcompat.widget.Toolbar
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
}

