package com.uniimarket.app.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uniimarket.app.R
import com.uniimarket.app.home.model.NewPostFilterData
import kotlinx.android.synthetic.main.sell_category_data.view.*

class NewPostFilterAdapter(
    var filterDataList: ArrayList<NewPostFilterData.Datum?>,
    var context: Context?,
    var productsFragment: ProductsFragment
) : RecyclerView.Adapter<NewPostFilterAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewPostFilterAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.sell_category_data,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return filterDataList.size
    }

    override fun onBindViewHolder(holder: NewPostFilterAdapter.ViewHolder, position: Int) {
        holder?.tv_category_name.text = filterDataList[position]?.name


        holder.rb_category.isChecked = filterDataList[position]?.filterChecked!!

        holder?.rb_category.setOnClickListener {
            //            clickCategory.onClickFilter(filterList[position].id,filterList[position].itemName)
            if (filterDataList[position]?.filterChecked!!) {
                filterDataList[position]?.filterChecked = false
                holder.rb_category.isChecked = false
            } else {
                filterDataList[position]?.filterChecked = true
                holder.rb_category.isChecked = true
            }

        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        public val rb_category = view.rb_category
        val tv_category_name = view.tv_category_name
    }

    interface FilterClickListener {
        fun onClickFilter(id: String?, name: String?)
    }

}
