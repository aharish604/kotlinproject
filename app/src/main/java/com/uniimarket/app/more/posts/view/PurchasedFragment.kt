package com.uniimarket.app.more.posts.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
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
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.more.posts.model.PurchaseData
import com.uniimarket.app.more.posts.presenter.PurchasePresenter

class PurchasedFragment : Fragment(), PurchasePresenter.PurchaseListener {


    var rv_posted: RecyclerView? = null
    var tv_items_posted: TextView? = null
    var purchasePresenter: PurchasePresenter? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater?.inflate(R.layout.posted_fragment, container, false)

        initialVariables(view)
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }


        when {
            Helper.post == "purchased" -> tv_items_posted?.text = "Items Purchased"
            Helper.post == "sold" -> tv_items_posted?.text = "Items Sold"
            Helper.post == "posted" -> tv_items_posted?.text = "Items Posted"
        }

        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE

        (context as DashboardActivity).ll_back?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                MyPostsFragment(), true, false
            )
        }

        when {
            Helper.post == "posted" -> {
                getPostedList()
            }
            Helper.post == "sold" -> {
                getSoldList()
            }
            Helper.post == "purchased" -> {
                getPurchasedList()
            }

        }


        return view
    }

    @SuppressLint("WrongConstant")
    private fun getPurchasedList() {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()

        purchasePresenter?.getPurchaseList()

    }

    private fun getSoldList() {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        purchasePresenter?.getSoldList()

    }


    private fun getPostedList() {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        purchasePresenter?.getPostedList()
    }

    private fun initialVariables(view: View?) {
        rv_posted = view?.findViewById(R.id.rv_posted)
        tv_items_posted = view?.findViewById(R.id.tv_items_posted)

        purchasePresenter = PurchasePresenter(this, context)
        progressDialog = ProgressDialog(context)
    }

    @SuppressLint("WrongConstant")
    override fun purchaseResponseSuccess(
        status: String,
        message: String?,
        purchaseDataList: ArrayList<PurchaseData.Datum>
    ) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }

        if (purchaseDataList.size > 0) {
            rv_posted?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rv_posted?.adapter = PurchasedAdapter(purchaseDataList, this, this, context)
        } else {
            val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)

            dialog.setContentView(R.layout.notificaiton_popup)
            val body = dialog.findViewById(R.id.imv_cancel) as ImageView
//        body.text = title
//        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
            val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView
            tv_security_text.text = message
            body.setOnClickListener {
                //            notificationPresenter?.getNotificationList()
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    override fun purchasseResponseFailure(status: String, message: String?) {

        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.notificaiton_popup)
        val body = dialog.findViewById(R.id.imv_cancel) as ImageView
//        body.text = title
//        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
        val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView
        tv_security_text.text = message
        body.setOnClickListener {
            //            notificationPresenter?.getNotificationList()
            dialog.dismiss()
        }

        dialog.show()

    }


}
