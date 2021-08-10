package com.uniimarket.app.notification.view

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
import com.uniimarket.app.Dashboard.view.PathWaysFragment
import com.uniimarket.app.R
import com.uniimarket.app.notification.modal.NotificationData
import com.uniimarket.app.notification.presenter.NotificationPresenter

class NotificationFragment : Fragment(), NotificationPresenter.NotificationListener,
    NotificationAdapter.NotificationAdapterOnClick {


    var rv_notifications: RecyclerView? = null
    var notificationPresenter: NotificationPresenter? = null
    var notificationDataList: ArrayList<NotificationData.Datum> = ArrayList()
    var tv_chat_contacts: TextView? = null

    var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.notification_fragment, container, false)

        initialVariables(view)
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
        (context as DashboardActivity).imv_search?.visibility = View.VISIBLE
        (context as DashboardActivity).imv_user_search?.visibility = View.GONE

        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE

        (context as DashboardActivity).ll_back?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                PathWaysFragment(), true, false
            )
        }

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }


        getNotificationsList()

        return view
    }

    @SuppressLint("WrongConstant")
    private fun getNotificationsList() {

        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()

        notificationPresenter?.getNotificationList()

    }


    private fun initialVariables(view: View?) {

        progressDialog = ProgressDialog(context)
        rv_notifications = view?.findViewById(R.id.rv_notifications)
        tv_chat_contacts = view?.findViewById(R.id.tv_chat_contacts)
        notificationPresenter = context?.let { NotificationPresenter(this, it) }
    }


    @SuppressLint("WrongConstant")
    override fun notificationResponseSuccess(
        status: String,
        message: String?,
        notificationDataList: ArrayList<NotificationData.Datum>
    ) {

        try {
            progressDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        this.notificationDataList = notificationDataList

        if (notificationDataList.size > 0) {
            rv_notifications?.visibility = View.VISIBLE
            tv_chat_contacts?.visibility = View.GONE

            rv_notifications?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rv_notifications?.adapter =
                NotificationAdapter(notificationDataList, this, this, context)
        } else {
            rv_notifications?.visibility = View.GONE
//            val dialog = Dialog(activity, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setCancelable(false)
//
//            dialog.setContentView(R.layout.notificaiton_popup)
//            val body = dialog.findViewById(R.id.imv_cancel) as ImageView
////        body.text = title
////        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
//            val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView
//            tv_security_text.text = message
//            body.setOnClickListener {
//                //            notificationPresenter?.getNotificationList()
//                dialog.dismiss()
//            }
//
//            dialog.show()

            tv_chat_contacts?.visibility = View.VISIBLE
        }

    }

    override fun notificationResponseFailure(status: String, message: String?) {
        progressDialog?.dismiss()
        rv_notifications?.visibility = View.GONE
//        val dialog = Dialog(activity, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//
//        dialog.setContentView(R.layout.notificaiton_popup)
//        val body = dialog.findViewById(R.id.imv_cancel) as ImageView
////        body.text = title
////        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
//        val tv_security_text = dialog.findViewById(R.id.tv_security_text) as TextView
//        tv_security_text.text = message
//        body.setOnClickListener {
//            //            notificationPresenter?.getNotificationList()
//            dialog.dismiss()
//        }
//
//        dialog.show()
        tv_chat_contacts?.text = "No Notifications"
        tv_chat_contacts?.visibility = View.VISIBLE
    }

    override fun onClick(
        item: String,
        notificationId: String,
        datum: NotificationData.Datum
    ) {

        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()

        notificationPresenter?.updateNotificationStatus(item, datum.senderId, datum.userId)

    }

    override fun notificationStatusResponse(status: String, message: String) {
        tv_chat_contacts?.visibility = View.GONE
        try {
            progressDialog?.dismiss()
        } catch (e: Exception) {
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
            notificationPresenter?.getNotificationList()
            dialog.dismiss()
        }

        dialog.show()

    }

}
