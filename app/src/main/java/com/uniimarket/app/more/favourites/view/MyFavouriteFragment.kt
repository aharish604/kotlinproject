package com.uniimarket.app.more.favourites.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.R
import com.uniimarket.app.categories.presenter.AddFavouritesPresenter
import com.uniimarket.app.more.MoreFragment
import com.uniimarket.app.more.favourites.model.MyFavouritesData
import com.uniimarket.app.more.favourites.presenter.MyFavouritesPresenter

class MyFavouriteFragment : Fragment(), MyFavouritesPresenter.MyFavouritesListener,
    MyFavouriteAdapter.AdapterOnClick,
    AddFavouritesPresenter.AddFavouritesListener {


    var rv_favourites: RecyclerView? = null
    var myFavouritesPresenter: MyFavouritesPresenter? = null
    var addFavouritesPresenter: AddFavouritesPresenter? = null
    var progressDialog: ProgressDialog? = null

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater?.inflate(R.layout.my_favourite_fragment, container, false)

        initialVariables(view)
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE

        (context as DashboardActivity).ll_back?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                MoreFragment(), true, false
            )
        }

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }


        getFavourtiesList()

//        myFavouritesPresenter?.getFavouritesList()
        return view
    }


    @SuppressLint("WrongConstant")
    private fun getFavourtiesList() {
        Log.e("fav status", "fav called")
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        myFavouritesPresenter?.getFavouritesList()

//        val favouritesList = ArrayList<MyFavouritesData>()//Creating an empty arraylist.
//        favouritesList.clear()
//        var categoriesData: MyFavouritesData = MyFavouritesData()
//        favouritesList.add(categoriesData)//Adding object in arraylist
//
//
//        categoriesData = MyFavouritesData(
//            R.drawable.ic_launcher_background, "$ 44.00", "Man T-Shirt",
//            "location", "posted date", "Not Sold"
//        )
//        favouritesList.add(categoriesData)
//
//        categoriesData = MyFavouritesData(
//            R.drawable.ic_launcher_background, "$ 44.00", "Man T-Shirt",
//            "location", "posted date", "Not Sold"
//        )
//        favouritesList.add(categoriesData)
//
//        categoriesData = MyFavouritesData(
//            R.drawable.ic_launcher_background, "$ 44.00", "Man T-Shirt",
//            "location", "posted date", "Sold Out"
//        )
//        favouritesList.add(categoriesData)
//
//        categoriesData = MyFavouritesData(
//            R.drawable.ic_launcher_background, "$ 4.00", "Man T-Shirt",
//            "location", "posted date", "Sold Out"
//        )
//        favouritesList.add(categoriesData)
//
//        categoriesData = MyFavouritesData(
//            R.drawable.ic_launcher_background, "$ 44.00", "Man T-Shirt",
//            "location", "posted date", "Not Sold"
//        )
//        favouritesList.add(categoriesData)//Adding object in arraylist


    }


    private fun initialVariables(view: View?) {
        progressDialog = ProgressDialog(context)
        rv_favourites = view?.findViewById(R.id.rv_favourites)
        myFavouritesPresenter = MyFavouritesPresenter(this, context)
        addFavouritesPresenter = AddFavouritesPresenter(this, context)
    }


    @SuppressLint("WrongConstant")
    override fun myFavouritesResponseSuccess(
        status: String,
        message: String?,
        favouritesList: java.util.ArrayList<MyFavouritesData.Datum>
    ) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        if (favouritesList.size > 0) {
            rv_favourites?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rv_favourites?.adapter =
                MyFavouriteAdapter(favouritesList, this, this, context)
        } else {
            val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)

            dialog.setContentView(R.layout.notificaiton_popup)
            val body = dialog.findViewById(R.id.imv_cancel) as ImageView
//        body.text = title
//        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
            val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView
            tv_security_text.text = "No Data Found"
            body.setOnClickListener {
                //            notificationPresenter?.getNotificationList()
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    override fun myFavouritesResponseFailure(status: String, message: String?) {

        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        val dialog = Dialog(activity, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.notificaiton_popup)
        val body = dialog.findViewById(R.id.imv_cancel) as ImageView
//        body.text = title
//        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
        val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView
        tv_security_text.text = "No Data Found"
        body.setOnClickListener {
            //            notificationPresenter?.getNotificationList()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onClick(item: Int, productId: String) {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        addFavouritesPresenter?.addFavourites(productId)
    }

    override fun onClickProductDetails(item: Int, friendId: MyFavouritesData.Datum) {
        (context as DashboardActivity).replaceFragment(
            R.id.frame_layout,
            FavouriteProductDetailsFragment(), true, false
        )
    }

    override fun addFavouritesResponse(status: String, message: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
//        getFavourtiesList()
    }

    override fun checkFavouritesResponse(status: String, message: String?, data: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

}
