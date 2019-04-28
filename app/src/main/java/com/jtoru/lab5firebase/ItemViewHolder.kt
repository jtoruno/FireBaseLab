package com.jtoru.lab5firebase

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_row.view.*

class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    fun bindToItem(item : Item ){
        itemView.textView.text = item.name
        itemView.textView2.text = item.amount
    }
}