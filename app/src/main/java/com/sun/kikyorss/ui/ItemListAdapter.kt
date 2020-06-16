package com.sun.kikyorss.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.kikyorss.logic.MyApplication
import com.sun.kikyorss.R
import com.sun.kikyorss.database.Item
import com.sun.kikyorss.logic.formatDate
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.adapter_layout.view.*

class ItemListAdapter(
    private val itemList: List<Item>,
    private val fragmentManager: FragmentManager
) :
    RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cell = itemView.cell!!
        val title = itemView.title!!
        val date = itemView.date!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount() = itemList.size + 1

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (position < itemList.size) {
            val item = itemList[position]
            holder.apply {
                title.text = item.title
                cell.setOnClickListener {
                    fragmentManager.beginTransaction().replace(
                        R.id.frag_container,
                        ArticleFragment(item)
                    ).addToBackStack(null).commit()
                }
            }
            item.pubDate?.let {
                holder.date.apply {
                    visibility = View.VISIBLE
                    text = formatDate(it)
                }
            }

        } else {
            holder.apply {
                title.text = ""
                date.visibility = View.GONE
                cell.setOnClickListener {
                    Toasty.info(MyApplication.appContext, "到结尾了哦~", Toasty.LENGTH_SHORT).show()
                }
            }
        }
    }
}