package com.uniimarket.app.sell.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uniimarket.app.R
import com.uniimarket.app.home.model.CategoriesData
import kotlinx.android.synthetic.main.sell_category_data.view.*
import java.util.*

class SellCategoryAdapter(
    var categoriesList: ArrayList<CategoriesData.Datum>,
    var context: Context?,
    var sellFragment: SellFragment,
    var clickCategory: CategoryClickListener
) : RecyclerView.Adapter<SellCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellCategoryAdapter.ViewHolder {
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

    override fun onBindViewHolder(holder: SellCategoryAdapter.ViewHolder, position: Int) {
        holder?.tv_category_name.text = categoriesList[position].name
        Log.e("name", categoriesList[position].name)

        holder?.rl_category.setOnClickListener {
            clickCategory.onClickCategory(categoriesList[position].id, categoriesList[position].name)
        }

        holder?.rb_category.setOnClickListener {
            //            Helper.categoriesData = categoriesList[position]
//            (context as DashboardActivity).replaceFragment(
//                R.id.frame_layout,
//                SubCategoriesFragment(), true, false
//            )

            clickCategory.onClickCategory(categoriesList[position].id, categoriesList[position].name)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        public val rb_category = view.rb_category
        val tv_category_name = view.tv_category_name
        val rl_category = view.rl_category
    }

    interface CategoryClickListener {
        fun onClickCategory(id: String?, name: String?)
    }

}