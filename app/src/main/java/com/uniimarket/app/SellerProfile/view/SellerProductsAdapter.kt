package com.uniimarket.app.SellerProfile.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.SellerProfile.model.SellerProfileData
import kotlinx.android.synthetic.main.my_favourites_data.view.*
import kotlinx.android.synthetic.main.my_favourites_data.view.imv_product_image
import kotlinx.android.synthetic.main.my_favourites_data.view.tv_product_cost
import kotlinx.android.synthetic.main.my_favourites_data.view.tv_product_distance
import kotlinx.android.synthetic.main.my_favourites_data.view.tv_product_name
import kotlinx.android.synthetic.main.my_favourites_data.view.tv_product_posted
import kotlinx.android.synthetic.main.seller_product_data.view.*
import java.util.*

class SellerProductsAdapter(
    val sellerProfileDataList: ArrayList<SellerProfileData.Datum>,
    val context: Context?
) : RecyclerView.Adapter<SellerProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.seller_product_data,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return sellerProfileDataList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tv_product_cost?.text = "${sellerProfileDataList[position].price}"
        holder?.tv_product_name?.text = sellerProfileDataList[position].productName
        var date: String = sellerProfileDataList[position].date?.split(" ")!![0]
        Log.e("pr date", sellerProfileDataList[position].date)
        holder?.tv_product_posted?.text = "Offer Posted: $date"
//        holder?.tv_product_distance?.text = sellerProfileDataList[position].productLocation
//        holder?.imv_product_image?.setBackgroundResource(sellerProfileDataList[position].productPics)


        if (context != null) {
            Glide.with(context).load(sellerProfileDataList[position].productPics)
                .into(holder?.imv_product_image)
        }


//        val geocoder = Geocoder(context, Locale.getDefault())
//        try {
//            var addresses = sellerProfileDataList[position].latitude?.toDouble()?.let {
//                sellerProfileDataList[position].longitude?.toDouble()?.let { it1 ->
//                    geocoder.getFromLocation(
//                        it,
//                        it1, 1
//                    )
//                }
//            }
//
//            var add: String? = null
//            try {
//                val obj = addresses?.get(0)
//                add = obj?.getAddressLine(0)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            Log.v("IGA", "Address " + add)
//            holder.tv_product_distance?.text = add
//        } catch (e: java.lang.Exception) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }

        holder.tv_product_distance?.text = sellerProfileDataList[position].location


        holder.ll_seller_product_data.setOnClickListener {

            Helper.sellerProductView = sellerProfileDataList[position]
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                SellerProductView(), true, false
            )

        }


//        ProductDetailsFragment

//        if (sellerProfileDataList[position].status.equals("Sale")) {
//            holder?.tv_sold_out.visibility = View.VISIBLE
//            holder?.tv_product_cost.setTextColor(Color.parseColor("#9A9A9A"))
//            holder?.tv_product_name.setTextColor(Color.parseColor("#9A9A9A"))
//            holder?.tv_product_posted.setTextColor(Color.parseColor("#9A9A9A"))
//            holder?.tv_product_distance.setTextColor(Color.parseColor("#9A9A9A"))
//            holder?.tv_sold_out.setTextColor(Color.parseColor("#9A9A9A"))
//        } else {
//            holder?.tv_sold_out.visibility = View.GONE
//            holder?.tv_product_cost.setTextColor(Color.parseColor("#000000"))
//            holder?.tv_product_name.setTextColor(Color.parseColor("#000000"))
//            holder?.tv_product_posted.setTextColor(Color.parseColor("#000000"))
//            holder?.tv_product_distance.setTextColor(Color.parseColor("#000000"))
//        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        public val tv_product_cost = view.tv_product_cost
        public val tv_product_name = view.tv_product_name
        public val imv_favourite = view.imv_favourite
        public val tv_product_distance = view.tv_product_distance
        public val tv_product_posted = view.tv_product_posted
        public val imv_product_image = view.imv_product_image
        val ll_seller_product_data: LinearLayout = view.ll_seller_product_data
        val tv_sold_out = view.tv_sold_out

    }
}
