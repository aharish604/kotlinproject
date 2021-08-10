package com.uniimarket.app.more

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Dashboard.view.PathWaysFragment
import com.uniimarket.app.Signin.view.SignInActivity
import com.uniimarket.app.more.About.view.AboutFragment
import com.uniimarket.app.more.contact.view.ContactUsFragment
import com.uniimarket.app.more.favourites.view.MyFavouriteFragment
import com.uniimarket.app.more.posts.view.MyPostsFragment
import com.uniimarket.app.more.profile.view.FullProfileFragment
import com.uniimarket.app.more.settings.view.SettingsFragment


class MoreFragment : Fragment() {

    var rl_aboutus: RelativeLayout? = null
    var rl_profile: RelativeLayout? = null
    var rl_settings: RelativeLayout? = null
    var rl_contact_us: RelativeLayout? = null
    var rl_my_posts: RelativeLayout? = null
    var rl_myfavourites: RelativeLayout? = null
    var rl_logout: RelativeLayout? = null
    var sharedPref: SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater?.inflate(com.uniimarket.app.R.layout.more_fragment, container, false)

        initialVariables(view)
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
        sharedPref = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(com.uniimarket.app.R.drawable.profile).into(it)
        }

        (context as DashboardActivity).imv_search?.visibility = View.VISIBLE
        (context as DashboardActivity).imv_user_search?.visibility = View.GONE

        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE

        (context as DashboardActivity).ll_back?.setOnClickListener {
            //            (context as DashboardActivity).ll_back?.visibility = View.GONE
            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                PathWaysFragment(), true, false
            )
        }

        rl_aboutus?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                AboutFragment(), true, false
            )
        }

        rl_profile?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                FullProfileFragment(), true, false
            )
        }

        rl_settings?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                SettingsFragment(), true, false
            )
        }

        rl_contact_us?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                ContactUsFragment(), true, false
            )

//            val intent = Intent(context, ContactUsActivity::class.java)
//            startActivity(intent)

        }

        rl_my_posts?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                MyPostsFragment(), true, false
            )
        }

        rl_myfavourites?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                MyFavouriteFragment(), true, false
            )
        }

        rl_logout?.setOnClickListener {


            val builder1 = AlertDialog.Builder(context!!)
            builder1.setTitle("Logout")
            builder1.setMessage("Are you sure you want to log out?")
            builder1.setCancelable(true)

            builder1.setPositiveButton(
                "Yes"
            ) { dialog, id ->

                sharedPref?.edit()?.clear()
                sharedPref?.edit()?.commit()
                sharedPref?.edit()?.apply()

//                val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
                val editor = sharedPref?.edit()
                editor?.clear()
                editor?.commit()
                editor?.apply()

                val intent = Intent(context, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                dialog.cancel()
            }

            builder1.setNegativeButton(
                "No"
            ) { dialog, id -> dialog.cancel() }

            val alert11 = builder1.create()
            alert11.show()
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false

            //            (context as DashboardActivity).replaceFragment(R.id.frame_layout, SubCategoriesFragment(), true, false)
        }

        return view
    }

    private fun initialVariables(view: View?) {
        rl_aboutus = view?.findViewById(com.uniimarket.app.R.id.rl_aboutus)
        rl_profile = view?.findViewById(com.uniimarket.app.R.id.rl_profile)
        rl_settings = view?.findViewById(com.uniimarket.app.R.id.rl_settings)
        rl_contact_us = view?.findViewById(com.uniimarket.app.R.id.rl_contact_us)
        rl_my_posts = view?.findViewById(com.uniimarket.app.R.id.rl_my_posts)
        rl_myfavourites = view?.findViewById(com.uniimarket.app.R.id.rl_myfavourites)
        rl_logout = view?.findViewById(com.uniimarket.app.R.id.rl_logout)
    }

}
