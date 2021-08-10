package com.uniimarket.app.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.categories.view.SubCategoriesFragment
import com.uniimarket.app.home.model.CategoriesData
import kotlinx.android.synthetic.main.categories_data.view.*
import java.util.*

class CategoriesAdapter(
    var categoriesList: ArrayList<CategoriesData.Datum>,
    var context: Context?,
    var homeFragment: HomeFragment
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.categories_data,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: CategoriesAdapter.ViewHolder, position: Int) {
        holder?.tv_category.text = categoriesList[position].name

        context?.let { Glide.with(it).load(categoriesList[position].image).into(holder?.imv_category) }

        holder?.ll_category.setOnClickListener {
            Helper.homeScreen = "categories"
            Helper.categoriesData = categoriesList[position]
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                SubCategoriesFragment(), true, false
            )
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        public val ll_category = view.ll_category
        val imv_category = view.imv_category
        val tv_category = view.tv_category
    }


}
