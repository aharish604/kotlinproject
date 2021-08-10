package com.uniimarket.app.more.About.view

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.R
import com.uniimarket.app.more.About.model.AboutDatum
import com.uniimarket.app.more.About.presenter.AboutPresenter
import com.uniimarket.app.more.MoreFragment
import java.util.*


class AboutFragment : Fragment(), AboutPresenter.AboutListener {

    var txtAboutUs: TextView? = null

    var aboutPresenter: AboutPresenter? = null
    var progressDialog: ProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.about_fragment, container, false)
        initialVariables(view)
        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
        (context as DashboardActivity).ll_back?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                MoreFragment(), true, false
            )
        }

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }

        aboutPresenter = AboutPresenter(this)

        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        aboutPresenter?.getAboutData()

        return view
    }

    override fun AboutResponseSuccess(
        status: String,
        message: String?,
        signInList: ArrayList<AboutDatum>
    ) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        txtAboutUs?.text = signInList[0].getName()
    }

    override fun AboutResponseFailure(status: String, message: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun initialVariables(view: View?) {
        txtAboutUs = view?.findViewById(R.id.txtAboutUs)
        progressDialog = ProgressDialog(context)
    }

}
