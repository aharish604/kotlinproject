package com.uniimarket.app.sell.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uniimarket.app.R
import com.uniimarket.app.categories.model.SubCategoriesData
import kotlinx.android.synthetic.main.sell_category_data.view.*
import java.util.*

class SellSubCategoryAdapter(var categoriesList: ArrayList<SubCategoriesData.Datum>,
                             var context: Context?,
                             var sellFragment: SellFragment,
                             var clickCategory: SubCategoryClickListener
) : RecyclerView.Adapter<SellSubCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellSubCategoryAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.sell_category_data,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: SellSubCategoryAdapter.ViewHolder, position: Int) {
        holder?.tv_category_name.text = categoriesList[position].name

        holder.rl_category.setOnClickListener {
            clickCategory.onClickSubCategory(categoriesList[position].id,categoriesList[position].name)
        }

        holder?.rb_category.setOnClickListener {
            clickCategory.onClickSubCategory(categoriesList[position].id,categoriesList[position].name)
        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        public val rb_category = view.rb_category
        val tv_category_name = view.tv_category_name
        val rl_category = view.rl_category
    }

    interface SubCategoryClickListener {
        fun onClickSubCategory(id: String?, name: String?)
    }

}