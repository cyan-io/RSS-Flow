package com.sun.kikyorss.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.kikyorss.R
import com.sun.kikyorss.database.Channel
import kotlinx.android.synthetic.main.adapter_layout.view.*

class ChannelAdapter(private val channelList: MutableList<Channel>) :
    RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener

    inner class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cell = itemView.cell!!
        val description = itemView.description!!
        val title = itemView.title!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_layout, parent, false)
        return ChannelViewHolder(view)
    }

    override fun getItemCount() = channelList.size

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.title.text = channelList[position].title

        holder.description.apply {
            if(channelList[position].description.isNotBlank()){
                visibility = View.VISIBLE
                text = channelList[position].description
            }
        }
        onItemClickListener.let {
            holder.cell.apply {
                setOnClickListener {
                    onItemClickListener.onClick(it, position)
                }
                setOnLongClickListener {
                    onItemClickListener.onLongClick(it,position)
                    true
                }
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener=onItemClickListener
    }

    interface OnItemClickListener {
        fun onClick(view: View, position: Int)
        fun onLongClick(view: View, position: Int)
    }
}