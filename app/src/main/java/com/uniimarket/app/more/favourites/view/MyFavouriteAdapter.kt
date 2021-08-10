package com.uniimarket.app.more.favourites.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.more.favourites.model.MyFavouritesData
import kotlinx.android.synthetic.main.my_favourites_data.view.*
import java.util.*

class MyFavouriteAdapter(
    val myFavouritesDataList: ArrayList<MyFavouritesData.Datum>,
    val notificationFragment: MyFavouriteFragment,
    val adapterOnClick: MyFavouriteFragment,
    val context: Context?
) : RecyclerView.Adapter<MyFavouriteAdapter.ViewHolder>() {

    var boolean: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.my_favourites_data,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return myFavouritesDataList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder?.tv_product_cost?.text = myFavouritesDataList[position].price
        holder?.tv_product_name?.text = myFavouritesDataList[position].productName

        var str = myFavouritesDataList[position].date
        var delimiter = " "

        val parts = str?.split(delimiter)

        print(parts)

//
//        val geocoder = Geocoder(context, Locale.getDefault())
//        try {
//            var addresses = myFavouritesDataList[position].latitude?.toDouble()?.let {
//                myFavouritesDataList[position].longitude?.toDouble()?.let { it1 ->
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
////            add = add + "\n" + obj.getCountryName()
////            add = add + "\n" + obj.getCountryCode()
////            add = add + "\n" + obj.getAdminArea()
////            add = add + "\n" + obj.getPostalCode()
////            add = add + "\n" + obj.getSubAdminArea()
////            add = add + "\n" + obj.getLocality()
////            add = add + "\n" + obj.getSubThoroughfare()
//            Log.v("IGA", "Address " + add)
//            holder?.tv_product_distance?.text = add
////            et_ti_location?.setText(add)
//            // Toast.makeText(this, "Address=>" + add,
//            // Toast.LENGTH_SHORT).show();
//            // TennisAppActivity.showDialog(add);
//        } catch (e: java.lang.Exception) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }

        holder?.tv_product_distance?.text =  myFavouritesDataList[position].location

        holder?.tv_product_posted?.text = "Offer Posted: " + parts?.get(0)
//        holder?.tv_product_distance?.text = myFavouritesDataList[position].productLocation
//        holder?.imv_product_image?.setBackgroundResource(myFavouritesDataList[position].productPics)


//        if (context != null) {
//            Glide.with(context).load("http://13.234.112.106/uploads/" + myFavouritesDataList[position].productPics)
//                .placeholder(R.mipmap.ic_launcher)
//                .into(holder?.imv_product_image)
//        }

        if (context != null) {
            Glide.with(context).load(myFavouritesDataList[position].productPics)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder?.imv_product_image)
        }

//        if (myFavouritesDataList[position].status.equals("Active")) {
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

        if (myFavouritesDataList[position].productStatus.equals("Active")) {

//            if (myFavouritesDataList[position].)

            holder?.imv_favourite.setImageResource(R.drawable.favouriteon)

            holder?.tv_sold_out.visibility = View.GONE
            holder?.tv_product_cost.setTextColor(Color.parseColor("#000000"))
            holder?.tv_product_name.setTextColor(Color.parseColor("#000000"))
            holder?.tv_product_posted.setTextColor(Color.parseColor("#000000"))
            holder?.tv_product_distance.setTextColor(Color.parseColor("#000000"))
        } else {
            holder?.imv_favourite.setImageResource(R.drawable.sold_out)

            holder?.tv_sold_out.visibility = View.VISIBLE
            holder?.tv_product_cost.setTextColor(Color.parseColor("#9A9A9A"))
            holder?.tv_product_name.setTextColor(Color.parseColor("#9A9A9A"))
            holder?.tv_product_posted.setTextColor(Color.parseColor("#9A9A9A"))
            holder?.tv_product_distance.setTextColor(Color.parseColor("#9A9A9A"))
            holder?.tv_sold_out.setTextColor(Color.parseColor("#9A9A9A"))
        }

        holder?.imv_favourite.setOnClickListener {

            //            if (myFavouritesDataList[position].productStatus.equals("Active")) {


            if (myFavouritesDataList[position].productStatus.equals("Active")) {
                boolean = if (boolean) {
                    holder?.imv_favourite.setImageResource(R.drawable.favoriteoff)
                    false
                } else {
                    holder?.imv_favourite.setImageResource(R.drawable.favouriteon)
                    true
                }

                myFavouritesDataList[position].id?.let { it1 ->
                    adapterOnClick.onClick(
                        position,
                        it1
                    )
                }
            } else {

            }
//
//            } else {
//
//            }
        }

        holder.ll_my_favourite.setOnClickListener {
            if (myFavouritesDataList[position].productStatus.equals("Active")) {

                Helper.productFavouriteData = myFavouritesDataList[position]
                adapterOnClick.onClickProductDetails(
                    position,
                    myFavouritesDataList[position]
                )
            } else {
                Toast.makeText(
                    context,
                    "Sorry, this product is sold you can't view this product",
                    Toast.LENGTH_LONG
                ).show()
            }

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
        val tv_sold_out = view.tv_sold_out
        val ll_my_favourite = view.ll_my_favourite

    }

    interface AdapterOnClick {
        fun onClick(item: Int, friendId: String)
        fun onClickProductDetails(item: Int, friendId: MyFavouritesData.Datum)
    }
}
