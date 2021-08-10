package com.uniimarket.app.notification.view

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uniimarket.app.R
import com.uniimarket.app.notification.modal.NotificationData
import kotlinx.android.synthetic.main.notification_data.view.*
import java.util.*

class NotificationAdapter(
    val notificationDataList: ArrayList<NotificationData.Datum>,
    val notificationFragment: NotificationFragment,
    val notificationAdapterOnClick: NotificationAdapterOnClick,
    val context: Context?
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.notification_data,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return notificationDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tv_notification_name?.text = notificationDataList[position].message
        holder?.tv_notification_location?.text = notificationDataList[position].createdAt

        if (notificationDataList[position].ntype.equals("RS") ||
            notificationDataList[position].ntype.equals("RA")
        ) {

            if (notificationDataList[position].nstatus.equals("0")) {
                holder.ll_profile_request.visibility = View.VISIBLE
                holder.rl_notification.visibility = View.VISIBLE
                Log.e("rs", notificationDataList[position].message + "")
                Log.e("rs", notificationDataList[position].createdAt + "")
                Log.e("rs", notificationDataList[position].ntype + "")
            } else {
                holder.ll_profile_request.visibility = View.GONE
                holder.rl_notification.visibility = View.VISIBLE
            }
        } else {
            holder.ll_profile_request.visibility = View.GONE
            holder.rl_notification.visibility = View.VISIBLE
        }

        if (!this.notificationDataList[position].badgecount.equals("0")
        ) {
            holder.ll_notification_layout.setBackgroundColor(Color.parseColor("#00000000"))
        } else {
            holder.ll_notification_layout.setBackgroundColor(Color.parseColor("#9A9A9A"))
        }

        holder.ll_notification_layout?.setOnClickListener {
            if (notificationDataList[position].ntype.equals("RS") ||
                notificationDataList[position].ntype.equals("RA")
            ) {

            } else {
//                Helper.cid = "0"
//                Helper.scid ="0"
//                (context as DashboardActivity).replaceFragment(
//                    R.id.frame_layout,
//                    ProductsFragment(), true, false
//                )
            }
        }

        holder.btn_accept.setOnClickListener {
            notificationAdapterOnClick.onClick(
                "1",
                notificationDataList[position].id.toString(),
                notificationDataList[position]
            )
        }

        holder.btn_reject.setOnClickListener {
            notificationAdapterOnClick.onClick(
                "0",
                notificationDataList[position].id.toString(),
                notificationDataList[position]
            )
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        public val tv_notification_location = view.tv_notification_location
        public val tv_notification_name = view.tv_notification_name
        public val ll_notification_layout = view.ll_notification_layout
        val ll_profile_request = view.ll_accept_reject
        val btn_accept = view.btn_accept
        val btn_reject = view.btn_reject
        val rl_notification = view.rl_notification

    }

    interface NotificationAdapterOnClick {
        fun onClick(
            item: String,
            notificationId: String,
            datum: NotificationData.Datum
        )
    }
}
