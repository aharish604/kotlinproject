package com.uniimarket.app.categories.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uniimarket.app.R
import com.uniimarket.app.categories.model.SubCategoriesData
import kotlinx.android.synthetic.main.sub_categories_data.view.*
import java.util.*

class SubCategoriesAdapter(
    val subCategoriesDataList: ArrayList<SubCategoriesData.Datum>,
    val adapterOnClick: AdapterOnClick,
    val context: Context?
) :

    RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder>() {


    var count: Int = 0

    override fun getItemCount(): Int {
        return subCategoriesDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tv_category_item_name?.text = subCategoriesDataList[position].name

        holder.cv_category.setOnClickListener {
            count = position
//            subCategoriesDataList[position].name?.let { it1 -> callCategoryItem(it1) }

            subCategoriesDataList[position].id?.let { it1 ->
                subCategoriesDataList[position].name?.let { it2 ->
                    callCategoryItem(
                        it2,
                        it1
                    )
                }
            }
//            for (i in 0..subCategoriesDataList.size) {
//                if (i == position) {
//                    holder.cv_category.layoutParams = LinearLayout.LayoutParams(150, 100)
//                } else {
//                    holder.cv_category.layoutParams = LinearLayout.LayoutParams(150, 80)
//                }
//            }
        }

//        if (position == 0) {
//        subCategoriesDataList[position].name?.let {
//            subCategoriesDataList[position].id?.let { it1 ->
//                callCategoryItem(
//                    it,
//                    it1
//                )
//            }
//        }
//        }

    }

    private fun callCategoryItem(subCategoryName: String, subCategoryId: String) {

        adapterOnClick.onClick(subCategoryName, subCategoryId)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.sub_categories_data,
                parent,
                false
            )
        )
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        public val tv_category_item_name = view.tv_category_item_name
        val cv_category = view.cv_category
    }

    interface AdapterOnClick {
        fun onClick(item: String, subCategoryId: String)
    }
}
