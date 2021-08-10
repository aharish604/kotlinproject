package com.uniimarket.app.chat.view

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uniimarket.app.R
import com.uniimarket.app.chat.model.ChatViewData
import kotlinx.android.synthetic.main.chat_view_item_data.view.*
import java.util.*

class ChatViewAdapter(
    var chatViewDataList: ArrayList<ChatViewData.Datum>,
    var context: Context?,
    var profilepic: String?
) : RecyclerView.Adapter<ChatViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.chat_view_item_data,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return chatViewDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val sharedPref: SharedPreferences = context?.getSharedPreferences("uniimarket", Context.MODE_PRIVATE)!!

        if (sharedPref?.getString("id", null).equals(chatViewDataList[position].toId)) {

            holder.ll_friend.visibility = View.VISIBLE
            holder.ll_self.visibility = View.GONE
            holder?.tv_chat_friend?.text = chatViewDataList[position].description

            Glide.with(context!!).load(profilepic)
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(holder?.civ_friend)

        } else {
            holder.ll_friend.visibility = View.GONE
            holder.ll_self.visibility = View.VISIBLE
            holder?.tv_chat_self?.text = chatViewDataList[position].description

            context?.let {
                Glide.with(it).load(sharedPref?.getString("profilepic", null))
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(holder?.civ_self)
            }!!
        }


    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val ll_friend = view.ll_friend
        val tv_chat_friend = view.tv_chat_friend
        val civ_friend = view.civ_friend
        val ll_self = view.ll_self
        val tv_chat_self = view.tv_chat_self
        val civ_self = view.civ_self
    }

}