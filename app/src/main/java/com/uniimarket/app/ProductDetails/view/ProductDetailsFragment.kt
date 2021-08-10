package com.uniimarket.app.ProductDetails.view

import android.app.Dialog
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.SellerProfile.view.SellerProfileFragment
import com.uniimarket.app.categories.presenter.AddFavouritesPresenter
import com.uniimarket.app.categories.view.SubCategoriesFragment
import com.uniimarket.app.chat.view.ChatViewFragment
import com.uniimarket.app.home.view.ProductsFragment
import com.uniimarket.app.more.favourites.view.FavouriteProductDetailsFragment
import java.util.*

class ProductDetailsFragment : Fragment(), AddFavouritesPresenter.AddFavouritesListener {


    var cv_selleres_profile: CardView? = null
    var btn_sellers_profile: Button? = null
    var btn_contact_seller: Button? = null
    var cv_contact_seller: CardView? = null
    var tv_product_name: TextView? = null
    var tv_product_color: TextView? = null
    var tv_product_cost: TextView? = null
    var tv_product_location: TextView? = null
    var tv_product_date: TextView? = null
    var tv_product_description: TextView? = null
    var imv_product: ImageView? = null
    var imv_favourites: ImageView? = null
    var addFavouritesPresenter: AddFavouritesPresenter? = null
    var tv_product_price_type: TextView? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.product_fragment, container, false)

        initialVariables(view)
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
        checkFavourites()

        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE
        (context as DashboardActivity).ll_back?.setOnClickListener {

            if (Helper.homeScreen == "categories") {
                (context as DashboardActivity).replaceFragment(
                    R.id.frame_layout,
                    SubCategoriesFragment(), false, false
                )
            } else {
                if (Helper.productSeller.equals("favourite")){
                    (context as DashboardActivity).replaceFragment(
                        R.id.frame_layout,
                        FavouriteProductDetailsFragment(), false, false
                    )
                } else {
                    Log.e("pdf", "pdf");
                    (context as DashboardActivity).replaceFragment(
                        R.id.frame_layout,
                        ProductsFragment(), false, false
                    )
                }
            }


        }

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }


        btn_sellers_profile?.setOnClickListener {
            Helper.productSeller = ""

            try {
                if (Helper.productFavouriteData.uid == "") {
                    status("No User found")
                } else {

                    (context as DashboardActivity).addFragment(
                        R.id.frame_layout,
                        SellerProfileFragment(), true, false
                    )
                }
            }catch (e : java.lang.Exception){
                try {
                    if (Helper.productData.uid == "") {
                        status("No User found")
                    } else {

                        (context as DashboardActivity).addFragment(
                            R.id.frame_layout,
                            SellerProfileFragment(), true, false
                        )
                    }
                }catch (e : java.lang.Exception){

                }
            }
        }

        imv_favourites?.setOnClickListener {
            addFavourites()

        }

        imv_product?.let {
            try {

                Glide.with(context as DashboardActivity).load(Helper.productData.productPics).into(
                    it
                )
            } catch (e: Throwable) {
                try {
                    Glide.with(context as DashboardActivity).load(Helper.productData.productPics)
                        .into(
                            it
                        )
                } catch (e: Throwable) {
                    e.printStackTrace()
                    Glide.with(context as DashboardActivity)
                        .load(Helper.productFavouriteData.productPics).into(
                            it
                        )
                }
            }
        }

        try {

            tv_product_name?.text = Helper.productData.productName
            tv_product_cost?.text = "${Helper.productData.price}"
            tv_product_location?.text = Helper.productData.longitude + Helper.productData.longitude
            tv_product_date?.text = Helper.productData.date?.split(" ")?.get(0)
            tv_product_price_type?.text = Helper.productData.setprice
            tv_product_description?.text = Helper.productData.description
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

            try {
                tv_product_name?.text = Helper.productData.productName
                tv_product_cost?.text = "$ ${Helper.productData.price}"
                tv_product_location?.text =
                    Helper.productData.longitude + Helper.productData.longitude
                tv_product_date?.text = Helper.productData.date?.split(" ")?.get(0)
                tv_product_price_type?.text = Helper.productData.setprice
                tv_product_description?.text = Helper.productData.description
            } catch (e: Throwable) {
                tv_product_name?.text = Helper.productFavouriteData.productName
                tv_product_cost?.text = "$ ${Helper.productFavouriteData.price}"
                tv_product_location?.text =
                    Helper.productFavouriteData.longitude + Helper.productFavouriteData.longitude
                tv_product_date?.text = Helper.productFavouriteData.date?.split(" ")?.get(0)
                tv_product_price_type?.text = Helper.productFavouriteData.setprice
                tv_product_description?.text = Helper.productFavouriteData.description
            }


        }
        getAddress()

        return view
    }

    fun status(message: String) {
        val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(com.uniimarket.app.R.layout.notificaiton_popup)
        val body = dialog.findViewById(com.uniimarket.app.R.id.imv_cancel) as ImageView
//        body.text = title
//        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
        val tv_security_text =
            dialog.findViewById(com.uniimarket.app.R.id.tv_security_text) as TextView
        tv_security_text.text = message
        body.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun checkFavourites() {


        try {

            addFavouritesPresenter?.checkFavourites(Helper.productData.id)
        } catch (e: java.lang.Exception) {
//            addFavouritesPresenter?.checkFavourites(Helper.productData.id)
        }
    }

    private fun getAddress() {

        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            var addresses = Helper.productData.latitude?.toDouble()?.let {
                Helper.productData.longitude?.toDouble()?.let { it1 ->
                    geocoder.getFromLocation(
                        it,
                        it1, 1
                    )
                }
            }

            var add: String? = null
            try {

                val obj = addresses?.get(0)
                add = obj?.getAddressLine(0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
//            add = add + "\n" + obj.getCountryName()
//            add = add + "\n" + obj.getCountryCode()
//            add = add + "\n" + obj.getAdminArea()
//            add = add + "\n" + obj.getPostalCode()
//            add = add + "\n" + obj.getSubAdminArea()
//            add = add + "\n" + obj.getLocality()
//            add = add + "\n" + obj.getSubThoroughfare()
            Log.v("IGA", "Address " + add)
            tv_product_location?.text = add
//            et_ti_location?.setText(add)
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();
            // TennisAppActivity.showDialog(add);
        } catch (e: java.lang.Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            try {

                var addresses = Helper.productData.latitude?.toDouble()?.let {
                    Helper.productData.longitude?.toDouble()?.let { it1 ->
                        geocoder.getFromLocation(
                            it,
                            it1, 1
                        )
                    }
                }

                var add: String? = null
                try {

                    val obj = addresses?.get(0)
                    add = obj?.getAddressLine(0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
//            add = add + "\n" + obj.getCountryName()
//            add = add + "\n" + obj.getCountryCode()
//            add = add + "\n" + obj.getAdminArea()
//            add = add + "\n" + obj.getPostalCode()
//            add = add + "\n" + obj.getSubAdminArea()
//            add = add + "\n" + obj.getLocality()
//            add = add + "\n" + obj.getSubThoroughfare()
                Log.v("IGA", "Address " + add)
                tv_product_location?.text = add
//            et_ti_location?.setText(add)
                // Toast.makeText(this, "Address=>" + add,
                // Toast.LENGTH_SHORT).show();
                // TennisAppActivity.showDialog(add);
//            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        btn_contact_seller?.setOnClickListener {

            //            (context as DashboardActivity).replaceFragment(
//                R.id.frame_layout,
//                ChatFragment(), false, false
//            )

//            var chatContactsData: ChatContactsData.Datum = categoriesTypeList[position]
////            val intent = Intent(context, ChatViewActivity::class.java)
////            try {
////                intent.putExtra("uid", Helper.productData.uid)
////            }catch (e:java.lang.Exception){
////                e.printStackTrace()
////                intent.putExtra("uid", Helper.productFavouriteData.uid)
////            }
//            context?.startActivity(intent)

            try {
                (context as DashboardActivity).replaceFragment(
                    R.id.frame_layout,
                    ChatViewFragment(Helper.productData.uid + ""), false, false
                )
            } catch (e: java.lang.Exception) {
                (context as DashboardActivity).replaceFragment(
                    R.id.frame_layout,
                    ChatViewFragment(Helper.productFavouriteData.uid + ""), false, false
                )
            }
        }

    }

    private fun addFavourites() {

        try {
            progressDialog?.setMessage("Fetching Data...")
            progressDialog?.setCancelable(false)
            progressDialog?.show()
            addFavouritesPresenter?.addFavourites(Helper.productData.id)
        } catch (e: java.lang.Exception) {
            addFavouritesPresenter?.addFavourites(Helper.productData.id)
        }
    }

    private fun initialVariables(view: View?) {
//        cv_selleres_profile = view?.findViewById(R.id.cv_selleres_profile)
        btn_sellers_profile = view?.findViewById(R.id.btn_sellers_profile)
        btn_contact_seller = view?.findViewById(R.id.btn_contact_seller)
//        cv_contact_seller = view?.findViewById(R.id.cv_contact_seller)
        tv_product_name = view?.findViewById(R.id.tv_product_name)
        tv_product_color = view?.findViewById(R.id.tv_product_color)
        tv_product_cost = view?.findViewById(R.id.tv_product_cost)
        tv_product_location = view?.findViewById(R.id.tv_product_location)
        tv_product_date = view?.findViewById(R.id.tv_product_date)
        tv_product_description = view?.findViewById(R.id.tv_product_description)
        imv_product = view?.findViewById(R.id.imv_product)
        imv_favourites = view?.findViewById(R.id.imv_favourites)
        tv_product_price_type = view?.findViewById(R.id.tv_product_price_type)
        addFavouritesPresenter = AddFavouritesPresenter(this, context)

        progressDialog = ProgressDialog(context)
    }


    override fun addFavouritesResponse(status: String, message: String?) {

        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        if (message == "0") {
            imv_favourites?.setImageResource(R.drawable.favoriteoff)
        } else {
            imv_favourites?.setImageResource(R.drawable.favouriteon)
        }

    }


    override fun checkFavouritesResponse(status: String, message: String?, data: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        if (data == "1") {
            imv_favourites?.setImageResource(R.drawable.favouriteon)
        } else {
            imv_favourites?.setImageResource(R.drawable.favoriteoff)
        }
    }

}
