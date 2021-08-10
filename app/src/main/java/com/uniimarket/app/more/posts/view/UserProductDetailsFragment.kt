package com.uniimarket.app.more.posts.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.chat.model.ChatContactsData
import com.uniimarket.app.chat.presenter.ChatContactsPresenter
import com.uniimarket.app.more.posts.model.AllUsersFriendData
import com.uniimarket.app.more.posts.presenter.ProductDeletePresenter
import com.uniimarket.app.sell.view.SellFragment
import java.util.*
import kotlin.collections.ArrayList

class UserProductDetailsFragment : Fragment(), ProductDeletePresenter.ProductDeleteListener,
    ChatContactsPresenter.ChatContactsListener, ItemSoldAdapter.AdapterOnClick {


    var btn_delete_post: Button? = null
    var btn_item_sold: Button? = null
    var imv_edit: ImageView? = null
    var imv_product: ImageView? = null
    var tv_product_name: TextView? = null
    var tv_product_cost: TextView? = null
    var tv_product_location: TextView? = null
    var tv_product_date: TextView? = null
    var tv_product_description: TextView? = null
    var productDeletePresenter: ProductDeletePresenter? = null
    var chatContactsPresenter: ChatContactsPresenter? = null
    var chatContactsDataList: ArrayList<AllUsersFriendData.Datum> = ArrayList()
    var adapter: ItemSoldAdapter? = null
    var sharedPref: SharedPreferences? = null
    var dialog1: Dialog? = null
    var progressDialog: ProgressDialog? = null

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.user_product_detail_fragment, container, false)

        initialVariables(view)
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE
        (context as DashboardActivity).ll_back?.setOnClickListener {

            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                PurchasedFragment(), false, false
            )
        }


        getAllFriendsList()
//        getFriendsList()


        imv_edit?.setOnClickListener {

            Helper.edit = "Edit"

            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                SellFragment(), true, false
            )

        }

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }


        imv_product?.let {
            try {

                Glide.with(context as DashboardActivity)
                    .load(Helper.purchaseProductData.productPics).into(
                        it
                    )
            } catch (e: Exception) {
//                Glide.with(context as DashboardActivity).load(Helper.filterProductData.productPics).into(
//                    it
//                )
            }
        }

        try {

            tv_product_name?.text = Helper.purchaseProductData.productName
            tv_product_cost?.text = "${Helper.purchaseProductData.price}"
            tv_product_location?.text =
                Helper.purchaseProductData.longitude + Helper.purchaseProductData.longitude
            tv_product_date?.text = Helper.purchaseProductData.date
            tv_product_description?.text = Helper.purchaseProductData.description
            tv_product_location?.text = Helper.purchaseProductData.location
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

            tv_product_name?.text = Helper.productData.productName
            tv_product_cost?.text = "${Helper.purchaseProductData.price}"
            tv_product_location?.text = Helper.productData.longitude + Helper.productData.longitude
            tv_product_date?.text = Helper.productData.date
            tv_product_description?.text = Helper.productData.description
            tv_product_location?.text = Helper.productData.location
        }

//        getAddress()

        if (Helper.post == "sold") {
            btn_item_sold?.visibility = View.GONE
            btn_delete_post?.visibility = View.GONE
            imv_edit?.visibility = View.GONE
        }

        btn_delete_post?.setOnClickListener {


            val builder1 = AlertDialog.Builder(context!!)
            builder1.setTitle("Delete Post")
            builder1.setMessage("Are you sure you want to delete the post?")
            builder1.setCancelable(true)

            builder1.setPositiveButton(
                "Yes"
            ) { dialog, id ->
                dialog.cancel()
                progressDialog?.setMessage("Loading...")
                progressDialog?.setCancelable(false)
                progressDialog?.show()
                productDeletePresenter?.deletePost(Helper.purchaseProductData.id)
            }

            builder1.setNegativeButton(
                "No"
            ) { dialog, id -> dialog.cancel() }

            val alert11 = builder1.create()
            alert11.show()
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false


        }

        btn_item_sold?.setOnClickListener {
            val builder1 = AlertDialog.Builder(context!!)
            builder1.setTitle("Item Sold")
            builder1.setMessage("Are you sure you want to sold the product?")
            builder1.setCancelable(true)

            builder1.setPositiveButton(
                "Yes"
            ) { dialog, id ->
                dialog.cancel()


                dialog1 = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
                dialog1?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog1?.setCancelable(false)

                dialog1?.setContentView(R.layout.purchase_layout)
                val body = dialog1?.findViewById(R.id.imv_cancel) as ImageView
                val rv_chat_contacts = dialog1?.findViewById(R.id.rv_chat_contacts) as RecyclerView
                val tv_security_text: TextView =
                    dialog1?.findViewById(R.id.tv_security_text) as TextView
//        body.text = title
//        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
//        val noBtn = dialog .findViewById(R.id.noBtn) as TextView
                body.setOnClickListener {
                    dialog1?.dismiss()

                    with(sharedPref!!.edit()) {
                        putString("popup", "true")
                        commit()
                    }
                }

                dialog1?.show()

                if (chatContactsDataList.size > 0) {
                    rv_chat_contacts?.visibility = View.VISIBLE
                    tv_security_text?.visibility = View.GONE
                } else {
                    rv_chat_contacts?.visibility = View.GONE
                    tv_security_text?.visibility = View.VISIBLE
                }

                adapter = ItemSoldAdapter(chatContactsDataList, context, this)
                rv_chat_contacts?.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                rv_chat_contacts?.adapter = adapter

            }

            builder1.setNegativeButton(
                "No"
            ) { dialog, id -> dialog.cancel() }

            val alert11 = builder1.create()
            alert11.show()
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false


        }

        return view
    }

    private fun getAllFriendsList() {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        chatContactsPresenter?.getAllContactsList("")
    }

    private fun getFriendsList() {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        chatContactsPresenter?.getChatContactsList()

    }

    private fun getAddress() {

        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            var addresses = Helper.purchaseProductData.latitude?.toDouble()?.let {
                Helper.purchaseProductData.longitude?.toDouble()?.let { it1 ->
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
//            var addresses = Helper.productData.latitude?.toDouble()?.let {
//                Helper.productData.longitude?.toDouble()?.let { it1 ->
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
//            tv_product_location?.text = add
//            et_ti_location?.setText(add)
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();
            // TennisAppActivity.showDialog(add);
//            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun initialVariables(view: View?) {
        btn_delete_post = view?.findViewById(R.id.btn_delete_post)
        btn_item_sold = view?.findViewById(R.id.btn_item_sold)
        imv_edit = view?.findViewById(R.id.imv_edit)
        imv_product = view?.findViewById(R.id.imv_product)
        tv_product_name = view?.findViewById(R.id.tv_product_name)
        tv_product_cost = view?.findViewById(R.id.tv_product_cost)
        tv_product_location = view?.findViewById(R.id.tv_product_location)
        tv_product_date = view?.findViewById(R.id.tv_product_date)
        tv_product_description = view?.findViewById(R.id.tv_product_description)
        sharedPref = context?.getSharedPreferences("uniimarket", 0)
        productDeletePresenter = ProductDeletePresenter(this, context)
        chatContactsPresenter = ChatContactsPresenter(this, context)

        progressDialog = ProgressDialog(context)
    }

    override fun productDeleteResponseSuccess(status: String, message: String?, data: Int?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        if (data == 1) {
            Toast.makeText(context, "Successfully deleted", Toast.LENGTH_LONG).show()
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                PurchasedFragment(), true, false
            )
        } else {
            Toast.makeText(context, "Please try again", Toast.LENGTH_LONG).show()
        }

    }

    override fun productDeleteResponseFailure(status: String, message: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        Toast.makeText(context, "Please try again", Toast.LENGTH_LONG).show()
    }

    override fun productSoldResponseSuccess(status: String, message: String?, data: Int?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        if (data == 1) {
            Toast.makeText(context, "Product sold successfully", Toast.LENGTH_LONG).show()
//            (context as DashboardActivity).replaceFragment(
//                R.id.frame_layout,
//                PurchasedFragment(), true, false
//            )

            btn_item_sold?.visibility = View.GONE
            btn_delete_post?.visibility = View.GONE

        } else {
            Toast.makeText(context, "Please try again", Toast.LENGTH_LONG).show()
        }

    }

    override fun productSoldResponseFailure(status: String, message: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        Toast.makeText(context, "Please try again", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("WrongConstant")
    override fun chatContactsSuccessResponse(
        status: String,
        message: String?,
        chatContactsList: ArrayList<ChatContactsData.Datum>
    ) {

        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun chatContactsFailureResponse(status: String, message: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun allContactsSuccessResponse(
        status: String,
        message: String?,
        chatContactsList: ArrayList<AllUsersFriendData.Datum?>
    ) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        val sharedPref: SharedPreferences =
            context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!
        var id: String = sharedPref?.getString("id", null)

        for (i in 0 until chatContactsList.size) {
            if ((chatContactsList[i]?.id)?.equals(id)!!) {

            } else {
                chatContactsList[i]?.let { chatContactsDataList.add(it) }
            }
        }


    }

    override fun allContactsFailureResponse(status: String, message: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onClick(item: Int, friendId: String) {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        productDeletePresenter?.itemSold(Helper.purchaseProductData.id, "sold", friendId)
        dialog1?.dismiss()
    }
}