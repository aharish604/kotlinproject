package com.uniimarket.app.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.ProductDetails.view.ProductDetailsFragment
import com.uniimarket.app.R
import com.uniimarket.app.categories.model.CategoriesTypeData
import kotlinx.android.synthetic.main.banner_product_data.view.*
import java.util.*

class BannerProductsAdapter(
    var categoriesList: ArrayList<CategoriesTypeData.Datum>,
    var context: Context?
) : RecyclerView.Adapter<BannerProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerProductsAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.banner_product_data,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: BannerProductsAdapter.ViewHolder, position: Int) {
        holder?.tv_sub_category_item_name.text = categoriesList[position].productName
        holder?.tv_sub_category_item_price.text = categoriesList[position].price

        context?.let {
            Glide.with(it)
                .load(categoriesList[position].productPics)
                .placeholder(R.mipmap.ic_launcher).into(holder?.imv_sub_category_item)
        }

        holder?.ll_category_item.setOnClickListener {
            Helper.productData = categoriesList[position]
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                ProductDetailsFragment(), true, false
            )
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        public val ll_category_item = view.ll_category_item
        val imv_sub_category_item = view.imv_sub_category_item
        val tv_sub_category_item_price = view.tv_sub_category_item_price
        val tv_sub_category_item_name = view.tv_sub_category_item_name
    }


}
