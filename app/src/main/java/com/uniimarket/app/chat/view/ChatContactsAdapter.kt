package com.uniimarket.app.chat.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.R
import com.uniimarket.app.chat.model.ChatContactsData
import kotlinx.android.synthetic.main.caht_contacts_item_data.view.*
import java.util.*

class ChatContactsAdapter(
    var categoriesTypeList: ArrayList<ChatContactsData.Datum>,
    var context: Context?
) : RecyclerView.Adapter<ChatContactsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.caht_contacts_item_data,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return categoriesTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder?.tv_contact_name?.text = categoriesTypeList[position].getContactName()
//        holder?.tv_contact_status?.text = categoriesTypeList[position].getContactStatus()
//        holder?.tv_contact_time?.text = categoriesTypeList[position].getContactTime()
//        holder?.tv_contact_unread?.text = categoriesTypeList[position].getContactUnreadCount()
//        holder?.cimv_contact_image?.setBackgroundResource(categoriesTypeList[position].getContactImage()!!)
//        holder?.tv_contact_time?.text = categoriesTypeList[position].getContactTime()

        holder?.tv_contact_name?.text = categoriesTypeList[position].firstname
        holder?.tv_contact_status?.text = categoriesTypeList[position].email
//        holder?.tv_contact_time?.text = categoriesTypeList[position].getContactTime()
//        holder?.tv_contact_unread?.text = categoriesTypeList[position].getContactUnreadCount()
//        holder?.cimv_contact_image?.setBackgroundResource(categoriesTypeList[position].getContactImage()!!)
//        holder?.tv_contact_time?.text = categoriesTypeList[position].getContactTime()

        holder?.tv_contact_unread.visibility = View.GONE
        holder?.tv_contact_time.visibility = View.GONE
        holder?.ll_contact_unread.visibility = View.GONE

        holder?.ll_chat_contact.setOnClickListener {
            var chatContactsData: ChatContactsData.Datum = categoriesTypeList[position]

//            val intent = Intent(context, ChatViewActivity::class.java)
////            intent.putExtra("chatContact",chatContactsData)
//            intent.putExtra("uid",chatContactsData.id)
//            context?.startActivity(intent)

            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                ChatViewFragment(chatContactsData.id.toString()), false, false
            )

//            context?.startActivity(Intent(context, ChatViewActivity::class.java))
        }

        context?.let {
            Glide.with(it).load(categoriesTypeList[position].profilepic)
                .centerCrop()
//                .fitCenter()
                .placeholder(R.drawable.profile)
//                .preload(150,150)
                .into(holder?.cimv_contact_image)
        }

    }

    fun updateList(spinnerDataList1: ArrayList<ChatContactsData.Datum>) {
        this.categoriesTypeList = spinnerDataList1
        notifyDataSetChanged()

    }



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        public val cimv_contact_image = view.cimv_contact_image
        public val ll_contact_unread = view.ll_contact_unread
        var tv_contact_unread = view.tv_contact_unread
        val tv_contact_name = view.tv_contact_name
        val tv_contact_status = view.tv_contact_status
        val ll_contact_time = view.ll_contact_time
        val tv_contact_time = view.tv_contact_time
        val ll_contacts = view.ll_contacts
        val ll_chat_contact = view.ll_chat_contact
    }

}
