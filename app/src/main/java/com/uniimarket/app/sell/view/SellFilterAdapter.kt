package com.uniimarket.app.sell.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uniimarket.app.R
import com.uniimarket.app.sell.model.FilterData
import kotlinx.android.synthetic.main.sell_category_data.view.*
import java.util.*

class SellFilterAdapter(var filterList: ArrayList<FilterData.Datum>,
                        var context: Context?,
                        var clickCategory: FilterClickListener
) : RecyclerView.Adapter<SellFilterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellFilterAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.sell_category_data,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: SellFilterAdapter.ViewHolder, position: Int) {
        holder?.tv_category_name.text = filterList[position].itemName

        holder.rl_category.setOnClickListener {
            clickCategory.onClickFilter(filterList[position].id,filterList[position].itemName)
        }

        holder?.rb_category.setOnClickListener {
                        clickCategory.onClickFilter(filterList[position].id,filterList[position].itemName)
//            if (filterList[position].filterChecked) {
//                filterList[position].filterChecked = false
//                holder.rb_category.isChecked = false
//            } else {
//                filterList[position].filterChecked = true
//                holder.rb_category.isChecked = true
//            }

        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        public val rb_category = view.rb_category
        val tv_category_name = view.tv_category_name
        val rl_category = view.rl_category
    }

    interface FilterClickListener {
        fun onClickFilter(id: String?, name: String?)
    }

}