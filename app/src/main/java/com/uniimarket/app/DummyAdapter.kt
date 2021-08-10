package com.uniimarket.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listitem_dummy.view.*

class DummyAdapter(
    val sellerProfileDataList: ArrayList<Int>,
    val text: ArrayList<ScreenDatum>,
    val context: Context?,
    var clickScreen: screenClickListener

) : RecyclerView.Adapter<DummyAdapter.ViewHolder>() {

    private var runOnce: RunOnce? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.listitem_dummy,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return sellerProfileDataList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == 0) {
            holder?.tv_product_cost?.text = text[0].getPage1()
            holder?.welcome.visibility=View.VISIBLE
            holder?.but.visibility=View.GONE
        }
        if (position == 1) {
            holder?.tv_product_cost?.text = text[0].getPage2()
            holder?.welcome.visibility=View.GONE
            holder?.but.visibility=View.GONE

        }
        if (position == 2) {
            holder?.tv_product_cost?.text = text[0].getPage3()
            holder?.welcome.visibility=View.GONE
            holder?.but.visibility=View.VISIBLE

        }
        holder?.
            image?.setImageResource(sellerProfileDataList[position])
        (holder?.but as View?)!!.setOnClickListener {

            clickScreen.onClickcreen(position)

        }



    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val tv_product_cost = view.tvDummy
        val image = view.imageDummy
        val welcome = view.welcome
        val but = view.btn_next


    }

    interface screenClickListener {
        fun onClickcreen(id: Int?)
    }
}
