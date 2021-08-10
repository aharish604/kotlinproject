package com.uniimarket.app.Dashboard.view

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.chat.view.ChatFragment
import com.uniimarket.app.home.model.CategoriesData
import com.uniimarket.app.home.presenter.CategoriesPresenter
import com.uniimarket.app.home.view.HomeFragment
import java.util.*

class PathWaysFragment : Fragment(), CategoriesPresenter.CategoriesListener {

    var ll_marketplace: LinearLayout? = null
    var ll_chat: LinearLayout? = null
    var ll_coming_soon: LinearLayout? = null
    var sharedPref: SharedPreferences? = null
    var categoriesPresenter: CategoriesPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater?.inflate(com.uniimarket.app.R.layout.path_fragment, container, false)
        (context as DashboardActivity).ll_back?.visibility = View.INVISIBLE

        initialVariables(view)

        updateFirebaseToken()


        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
        (context as DashboardActivity).imv_search?.visibility = View.VISIBLE
        (context as DashboardActivity).imv_user_search?.visibility = View.GONE

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }

        ll_marketplace?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                HomeFragment(), true, false
            )
        }

        try {
            if (sharedPref?.getString("popup", null) == "true") {

            } else {
                openDialog()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            openDialog()
        }

        ll_chat?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                ChatFragment(), true, false
            )
        }

        ll_coming_soon?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                ComingSoonFragment(), true, false
            )
        }



        return view
    }

    private fun updateFirebaseToken() {
        var token = ""
        val android_id = Settings.Secure.getString(
            context!!.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("pn", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                token = task.result!!.token

                Log.e("SA FB Token", token)
                Helper.fbToken = token
                //                        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
            })

        categoriesPresenter?.updateToken(
            android_id, sharedPref?.getString("id", null),
            sharedPref?.getString("email", null), token
        )

    }


    private fun openDialog() {

        val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dashboard_popup)
        val body = dialog.findViewById(R.id.imv_cancel) as ImageView
//        body.text = title
//        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
//        val noBtn = dialog .findViewById(R.id.noBtn) as TextView
        body.setOnClickListener {
            dialog.dismiss()

            with(sharedPref!!.edit()) {
                putString("popup", "true")
                commit()
            }
        }

        dialog.show()

    }

    private fun initialVariables(view: View?) {

        ll_marketplace = view?.findViewById(R.id.ll_marketplace)
        ll_chat = view?.findViewById(R.id.ll_chat)
        ll_coming_soon = view?.findViewById(R.id.ll_coming_soon)
        categoriesPresenter = CategoriesPresenter(this, context)
        sharedPref = context?.getSharedPreferences("uniimarket", 0)
    }

    override fun categoriesResponseSuccess(
        status: String,
        message: String?,
        signInList: ArrayList<CategoriesData.Datum>
    ) {


    }

    override fun categoriesResponseFailure(status: String, message: String?) {

    }

    override fun tokenSuccess(s: String, message: String?) {
        Log.e("token update", s + "")
    }


}
