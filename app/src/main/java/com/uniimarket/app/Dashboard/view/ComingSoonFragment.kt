package com.uniimarket.app.Dashboard.view

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.model.ComingSoonDatum
import com.uniimarket.app.Dashboard.presenter.ComingSoonPresenter
import com.uniimarket.app.R
import java.util.*

class ComingSoonFragment : Fragment(), ComingSoonPresenter.ComingSoonListener {

    var comingSoonInfo: TextView? = null
    var comingSoonPresenter: ComingSoonPresenter? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view =
            inflater?.inflate(com.uniimarket.app.R.layout.coming_soon_fragment, container, false)

        initialVariables(view)
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE
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
        comingSoonPresenter = ComingSoonPresenter(this)

        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        comingSoonPresenter?.getComingSoonData()
        return view
    }

    private fun initialVariables(view: View?) {
        progressDialog = ProgressDialog(context)
        comingSoonInfo = view?.findViewById(R.id.comingSoonInfo)

    }

    override fun ComingSoonResponseSuccess(
        status: String,
        message: String?,
        signInList: ArrayList<ComingSoonDatum>
    ) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        comingSoonInfo?.text=signInList[0].getDescription()
    }

    override fun ComingSoonResponseFailure(status: String, message: String?) {
        progressDialog?.dismiss()
    }
}
