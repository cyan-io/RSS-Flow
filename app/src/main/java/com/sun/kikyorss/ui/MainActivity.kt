package com.sun.kikyorss.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.sun.kikyorss.*
import com.sun.kikyorss.logic.CheckUpdate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var toolBar: androidx.appcompat.widget.Toolbar
        lateinit var mainFragmentManager: FragmentManager
        lateinit var mainActivityContext: Context
        var hasNotRefresh = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init_webView.loadData(null, null, null)
        mainFragmentManager=supportFragmentManager
        supportFragmentManager.beginTransaction().add(R.id.frag_container, ChannelListFragment())
            .commit()
        toolBar = toolbar
        mainActivityContext=this
        CheckUpdate(frag_container,this)
    }
}

