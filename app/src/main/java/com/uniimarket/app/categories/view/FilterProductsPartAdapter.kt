package com.uniimarket.app.categories.view

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
import kotlinx.android.synthetic.main.sub_categories_item_data.view.*

class FilterProductsPartAdapter(
    var categoriesTypeList: ArrayList<CategoriesTypeData.Datum>,
    var context: Context?
) : RecyclerView.Adapter<FilterProductsPartAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.sub_categories_item_data,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return categoriesTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tv_sub_category_item_price?.text = "${categoriesTypeList[position]?.price}"
        holder?.tv_sub_category_item_name?.text = categoriesTypeList[position]?.productName

        context?.let {
            Glide.with(it).load(categoriesTypeList[position]?.productPics)
                .centerCrop()
//                .fitCenter()
                .placeholder(R.mipmap.ic_launcher)
//                .preload(150,150)
                .into(holder?.imv_sub_category_item)
        }
        holder.ll_category_item.setOnClickListener {
            Helper.productData = categoriesTypeList[position]!!
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                ProductDetailsFragment(), true, false
            )
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        public val tv_sub_category_item_price = view.tv_sub_category_item_price
        public val tv_sub_category_item_name = view.tv_sub_category_item_name
        val ll_category_item = view.ll_category_item
        val imv_sub_category_item = view.imv_sub_category_item
    }

}
