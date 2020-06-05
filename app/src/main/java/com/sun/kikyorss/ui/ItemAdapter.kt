package com.sun.kikyorss.ui

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.kikyorss.R
import com.sun.kikyorss.database.Item
import com.sun.kikyorss.formatDate
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.adapter_layout.view.*

class ItemAdapter(val itemList: List<Item>, val mainActivity: MainActivity) :
    RecyclerView.Adapter<ItemAdapter.mViewHolder>() {

    inner class mViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cell = itemView.cell
        val title = itemView.title
        //val description = itemView.article_description
        val date = itemView.date
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_layout, parent, false)
        return mViewHolder(view)
    }

    override fun getItemCount() = itemList.size + 1

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        if (position < itemList.size) {
            holder.apply {
                title.text = itemList[position].title
                //description.text = itemList[position].description
            }
            if(itemList[position].pubDate.length>1){
                holder.date.visibility=View.VISIBLE
                holder.date.text= formatDate(itemList[position].pubDate)
            }
            holder.cell.setOnClickListener {
                mainActivity.supportFragmentManager.beginTransaction().replace(
                    R.id.frag_container,
                    ArticleFragment(itemList[position])
                ).addToBackStack(null).commit()
            }
        } else {
            holder.title.text=""
            holder.cell.setOnClickListener {
                Toasty.info(mainActivity, "到结尾了哦~", Toasty.LENGTH_SHORT).show()
            }
        }
    }
}