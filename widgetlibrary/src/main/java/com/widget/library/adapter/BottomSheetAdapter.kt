package com.widget.library.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.widget.library.R
import com.widget.library.entity.BottomSheetListEntity

import java.util.ArrayList


/**
 * Created by cuiyang on 16/8/11.
 */
class BottomSheetAdapter : RecyclerView.Adapter<BottomSheetAdapter.DefineViewHolder>() {

    val item_data = ArrayList<BottomSheetListEntity>()

    override fun getItemCount(): Int {
        return item_data.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: DefineViewHolder, position: Int) {
        viewHolder.setData(item_data[position].str)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_bottom_sheet, parent, false)
        return DefineViewHolder(view)
    }

    fun getItem_data(): List<BottomSheetListEntity> {
        return item_data
    }

    inner class DefineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var btnItem: Button

        init {
            btnItem = itemView.findViewById<View>(R.id.btn_item) as Button
        }

        fun setData(data: String) {
            btnItem.text = data
        }
    }

    fun setNewData(strings: List<BottomSheetListEntity>) {
        this.item_data.clear()
        this.item_data.addAll(strings)
        notifyDataSetChanged()
    }
}