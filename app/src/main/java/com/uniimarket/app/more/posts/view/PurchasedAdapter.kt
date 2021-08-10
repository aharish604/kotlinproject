package com.uniimarket.app.more.posts.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.more.posts.model.PurchaseData
import kotlinx.android.synthetic.main.my_favourites_data.view.*
import kotlinx.android.synthetic.main.my_favourites_data.view.imv_product_image
import kotlinx.android.synthetic.main.my_favourites_data.view.tv_product_cost
import kotlinx.android.synthetic.main.my_favourites_data.view.tv_product_distance
import kotlinx.android.synthetic.main.my_favourites_data.view.tv_product_name
import kotlinx.android.synthetic.main.my_favourites_data.view.tv_product_posted
import kotlinx.android.synthetic.main.posts_item_data.view.*
import java.util.*

class PurchasedAdapter(
    var purchaseDataList: ArrayList<PurchaseData.Datum>,
    var purchasedFragment: PurchasedFragment,
    var purchasedFragment1: PurchasedFragment,
    var context: Context?
) : RecyclerView.Adapter<PurchasedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.posts_item_data,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return purchaseDataList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tv_product_cost?.text = "${purchaseDataList[position].price}"
        holder?.tv_product_name?.text = purchaseDataList[position].productName
        holder?.tv_product_posted?.text = "Posted on: " + purchaseDataList[position].date?.split(" ")?.get(0)

//        when {
//            Helper.post == "purchased" -> tv_items_posted?.text = "Items Purchased"
//            Helper.post == "sold" -> tv_items_posted?.text = "Items Sold"
//            Helper.post == "posted" -> tv_items_posted?.text = "Items Posted"
//        }

        holder?.tv_product_name?.text = purchaseDataList[position].productName


//        val geocoder = Geocoder(context, Locale.getDefault())
//        try {
//            var addresses = purchaseDataList[position].latitude?.toDouble()?.let {
//                purchaseDataList[position].longitude?.toDouble()?.let { it1 ->
//                    geocoder.getFromLocation(
//                        it,
//                        it1, 1
//                    )
//                }
//            }
//
//            var add: String? = null
//            try {
//
//                val obj = addresses?.get(0)
//                add = obj?.getAddressLine(0)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            Log.v("IGA", "Address " + add)
//            holder.tv_product_distance?.text = add
//
//        } catch (e: java.lang.Exception) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//            holder.tv_product_distance?.text = purchaseDataList[position].latitude+" : "+purchaseDataList[position].longitude
//        }

        holder?.tv_product_distance?.text = purchaseDataList[position].location

//        holder?.tv_product_distance?.text = purchaseDataList[position].productLocation

        holder?.ll_product.setOnClickListener {

            if(Helper.post == "purchased"){

            }else {

                Helper.purchaseProductData = purchaseDataList[position]
                (context as DashboardActivity).replaceFragment(
                    R.id.frame_layout,
                    UserProductDetailsFragment(), true, false
                )
            }
        }

        context?.let {
            Glide.with(it).load(purchaseDataList[position].productPics).placeholder(R.mipmap.ic_launcher)
                .into(holder?.imv_product_image)
        }



    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        public val tv_product_cost = view.tv_product_cost
        public val tv_product_name = view.tv_product_name
        public val imv_favourite = view.imv_favourite
        public val tv_product_distance = view.tv_product_distance
        public val tv_product_posted = view.tv_product_posted
        public val imv_product_image = view.imv_product_image
        val ll_product = view.ll_product
        val tv_sold_out = view.tv_sold_out

    }

}
