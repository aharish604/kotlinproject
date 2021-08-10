package com.uniimarket.app.more.posts.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.more.MoreFragment

class MyPostsFragment : Fragment() {

    var rl_posted: RelativeLayout? = null
    var rl_sold: RelativeLayout? = null
    var rl_purchased: RelativeLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater ?.inflate(R.layout.my_posts_fragment, container, false)

        initialVariables(view)
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE

        (context as DashboardActivity).ll_back?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                MoreFragment(), true, false)
        }

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let { Glide.with(this)
            .load(sharedPref?.getString("profilepic", null))
            .placeholder(R.drawable.profile).into(it) }


        rl_posted?.setOnClickListener {
            Helper.post = "posted"
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                PurchasedFragment(), true, false)
        }

        rl_sold?.setOnClickListener {
            Helper.post = "sold"
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                PurchasedFragment(), true, false)
        }

        rl_purchased?.setOnClickListener {
            Helper.post = "purchased"
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                PurchasedFragment(), true, false)
        }

        return view
    }


    private fun initialVariables(view: View?) {
        rl_posted = view?.findViewById(R.id.rl_posted)
        rl_sold = view?.findViewById(R.id.rl_sold)
        rl_purchased = view?.findViewById(R.id.rl_purchased)
    }


}
