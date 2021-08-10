package com.uniimarket.app.SellerProfile.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.SellerProfile.model.SellerProfileData
import com.uniimarket.app.SellerProfile.presenter.SellerProfilePresenter
import com.uniimarket.app.chat.view.ChatViewFragment
import com.uniimarket.app.more.settings.view.SettingsFragment

class SellerUserFragment : Fragment(), SellerProfilePresenter.SellerProfileListener {


    var rv_seller_products: RecyclerView? = null
    var civ_profile: ImageView? = null
    var tv_user_name: TextView? = null
    var tv_user_university: TextView? = null
    var tv_user_bio: TextView? = null
    var btn_view_profile: Button? = null
    var imv_notification: ImageView? = null
    var sellerProfilePresenter: SellerProfilePresenter? = null
    var notificationStatus: Boolean = false
    var notificationRequest: String = "Off"
    var tv_chat: TextView? = null
    var imv_chat: ImageView? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.profile_fragment, container, false)

        initialVariables(view)
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }


        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE
        (context as DashboardActivity).ll_back?.setOnClickListener {
            Helper.friendId = ""
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                SettingsFragment(), false, false
            )
        }

        tv_chat?.setOnClickListener {

            if (Helper.friendId == "") {
                (context as DashboardActivity).replaceFragment(
                    R.id.frame_layout,
                    ChatViewFragment(Helper.productData.uid + ""), false, false
                )
            } else {
                (context as DashboardActivity).replaceFragment(
                    R.id.frame_layout,
                    ChatViewFragment(Helper.friendId + ""), false, false
                )
            }
        }

        imv_chat?.setOnClickListener {
            if (notificationRequest == "On") {
//                (context as DashboardActivity).replaceFragment(
//                    R.id.frame_layout,
//                    ChatFragment(), false, false
//                )

                if (Helper.friendId == "") {
                    (context as DashboardActivity).replaceFragment(
                        R.id.frame_layout,
                        ChatViewFragment(Helper.productData.uid + ""), false, false
                    )
                } else {
                    (context as DashboardActivity).replaceFragment(
                        R.id.frame_layout,
                        ChatViewFragment(Helper.friendId + ""), false, false
                    )
                }
            } else {
                val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)

                dialog.setContentView(R.layout.notificaiton_popup)
                val body = dialog.findViewById(R.id.imv_cancel) as ImageView
                val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView

                tv_security_text.text = "Please Turn ON notifications"

                body.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }
        }

        imv_notification?.setOnClickListener {
            var uid: String

            progressDialog?.setMessage("Loading...")
            progressDialog?.setCancelable(false)
            progressDialog?.show()

            if (Helper.friendId == "") {
                Helper.productData.uid?.let { it1 ->
                    sellerProfilePresenter?.updateNotificationStatus(
                        it1
                    )
                }
            } else {
                sellerProfilePresenter?.updateNotificationStatus(Helper.friendId)
            }

//            uid = try {
//                Helper.productData.uid.toString()
//            } catch (e: Exception) {
//                Helper.friendId.toString()
//            }
//            sellerProfilePresenter?.updateNotificationStatus(uid)
        }

        btn_view_profile?.setOnClickListener {
            if (notificationRequest == "On") {
                var uid: String
                progressDialog?.setMessage("Loading...")
                progressDialog?.setCancelable(false)
                progressDialog?.show()
                if (Helper.friendId == "") {
                    sellerProfilePresenter?.notificationRequestList(Helper.productData.uid)
                } else {
                    sellerProfilePresenter?.notificationRequestList(Helper.friendId)
                }

            } else {
                val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)

                dialog.setContentView(R.layout.notificaiton_popup)
                val body = dialog.findViewById(R.id.imv_cancel) as ImageView
                val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView

                tv_security_text.text = "Please Turn ON notifications"

                body.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }
        }

        getSellerDetails()

        getNotificationStatus()

        return view
    }

    private fun getNotificationStatus() {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        if (Helper.friendId == "") {
            Helper.productData.uid?.let { sellerProfilePresenter?.getNotificationStatus(it) }
        } else {
            sellerProfilePresenter?.getNotificationStatus(Helper.friendId)
        }

    }

    private fun getSellerDetails() {
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        if (Helper.friendId == "") {
//            Helper.productData.uid?.let { it1 -> sellerProfilePresenter?.getNotificationStatus(it1) }
            sellerProfilePresenter?.getSellerProfileDetails(Helper.productData.uid)
        } else {
//            sellerProfilePresenter?.updateNotificationStatus(Helper.friendId)
            sellerProfilePresenter?.getSellerProfileDetails(Helper.friendId)
        }

    }

    private fun initialVariables(view: View?) {
        tv_chat = view?.findViewById(R.id.tv_chat)
        imv_chat = view?.findViewById(R.id.imv_chat)
        rv_seller_products = view?.findViewById(R.id.rv_seller_products)
        civ_profile = view?.findViewById(R.id.civ_profile)
        tv_user_name = view?.findViewById(R.id.tv_user_name)
        tv_user_university = view?.findViewById(R.id.tv_user_university)
        tv_user_bio = view?.findViewById(R.id.tv_user_bio)
        btn_view_profile = view?.findViewById(R.id.btn_view_profile)
        imv_notification = view?.findViewById(R.id.imv_notification)
        sellerProfilePresenter = SellerProfilePresenter(this, context)
        progressDialog = ProgressDialog(context)
    }


    @SuppressLint("SetTextI18n", "WrongConstant")
    override fun sellProfileResponseSuccess(
        status: Boolean,
        message: String?,
        subCategoryDataList: SellerProfileData.Records?,
        sellerProfileDataList: ArrayList<SellerProfileData.Datum>?
    ) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }

        var firstName: String = subCategoryDataList?.firstname!!
        var lastName: String = subCategoryDataList?.lastname!!
        var university: String = subCategoryDataList?.university!!
        var profilePic: String = subCategoryDataList?.profilepic!!


        rv_seller_products?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_seller_products?.adapter =
            sellerProfileDataList?.let { SellerProductsAdapter(it, context) }


        tv_user_name?.text = "${firstName + " " + lastName}"

        if (university == "" || university == null) {
            tv_user_university?.text = "Not Mentioned"
        } else {
            tv_user_university?.text = university
        }

        tv_user_bio?.text = subCategoryDataList?.bio.toString()!!

        if (subCategoryDataList?.bio.toString() == "" || subCategoryDataList?.bio.toString() == null) {
            tv_user_bio?.text = "Not Mentioned"
        } else {
            tv_user_bio?.text = subCategoryDataList?.bio.toString()!!
        }

        context?.let {
            civ_profile?.let { it1 ->
                Glide.with(it).load(profilePic)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(it1)
            }
        }


    }

    override fun sellProfileResponseFailure(status: Boolean, message: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun sellNotificationResponseFailure(status: Boolean, message: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun sellNotificationResponseSuccess(status: Boolean, data: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        if (data == "0") {
            notificationRequest = "Off"
            imv_notification?.setImageDrawable(resources.getDrawable(R.drawable.notification_off))
        } else {
            notificationRequest = "On"
            imv_notification?.setImageDrawable(resources.getDrawable(R.drawable.notification_on))
        }
//        notificationStatus = if (data.equals("0")) {
//            imv_notification?.setImageDrawable(resources.getDrawable(R.drawable.notification_off))
//            notificationRequest = "Off"
//            false
//        } else {
//            imv_notification?.setImageDrawable(resources.getDrawable(R.drawable.notification_on))
//            notificationRequest = "On"
//            true
//        }

    }

    override fun sellUpdateNotificationResponse(status: Boolean, message: String?, data: String?) {
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
        val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView

//data 0-> off , 1-> ON

        if (data == "0") {
            tv_security_text.text = "Notifications Off"
            notificationRequest = "Off"
        } else {
            tv_security_text.text = "Notifications On"
            notificationRequest = "On"
        }

        body.setOnClickListener {
            if (data == "0") {
                notificationRequest = "Off"
                imv_notification?.setImageDrawable(resources.getDrawable(R.drawable.notification_off))
            } else {
                notificationRequest = "On"
                imv_notification?.setImageDrawable(resources.getDrawable(R.drawable.notification_on))
            }
            dialog.dismiss()
        }

        dialog.show()

    }

    override fun notificationRequestResponse(status: Boolean, message: String?, data: String?) {
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
        val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView

//data 0-> off , 1-> ON

//        if (data == "0"){
        tv_security_text.text = message
//            notificationRequest = "Off"
//        } else{
//            tv_security_text.text = "Notifications On"
//            notificationRequest = "On"
//        }

        body.setOnClickListener {
            //            if (data == "0"){
//                notificationRequest = "Off"
//                imv_notification?.setImageDrawable(resources.getDrawable(R.drawable.notification_off))
//            } else{
//                notificationRequest = "On"
//                imv_notification?.setImageDrawable(resources.getDrawable(R.drawable.notification_on))
//            }
            dialog.dismiss()
        }

        dialog.show()

    }

    override fun notificationRequestResponseFailure(status: Boolean, message: String?) {
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
        val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView


//data 0-> off , 1-> ON

//        if (data == "0"){
//            tv_security_text.text = "Notifications Off"
//            notificationRequest = "Off"
//        } else{
//            tv_security_text.text = "Notifications On"
//            notificationRequest = "On"
//        }

        body.setOnClickListener {
            //            if (data == "0"){
//                notificationRequest = "Off"
//                imv_notification?.setImageDrawable(resources.getDrawable(R.drawable.notification_off))
//            } else{
//                notificationRequest = "On"
//                imv_notification?.setImageDrawable(resources.getDrawable(R.drawable.notification_on))
//            }
            dialog.dismiss()
        }

        dialog.show()
    }
}
