package com.uniimarket.app.more.contact.view

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.R
import com.uniimarket.app.more.MoreFragment
import com.uniimarket.app.more.contact.presenter.ContactUsPresenter

class ContactUsFragment : Fragment(), ContactUsPresenter.ContactUsListener {


    var btn_submit: Button? = null
    var et_user_name: EditText? = null
    var et_unii_mail: EditText? = null
    var et_university: EditText? = null
    var et_course: EditText? = null
    var et_query: EditText? = null
    var contactUsPresenter: ContactUsPresenter? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater?.inflate(R.layout.contact_us_fragment, container, false)
        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE

        initialVariables(view)

        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE

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


        btn_submit?.setOnClickListener {
            sendContactDetails()
        }


        et_user_name?.setText(sharedPref?.getString("firstname", null))
        et_university?.setText(sharedPref?.getString("university", null))
        et_course?.setText(sharedPref?.getString("course", null))
        et_unii_mail?.setText(sharedPref?.getString("email", null))

        et_user_name?.isEnabled = false
        et_university?.isEnabled = false
        et_course?.isEnabled = false
        et_unii_mail?.isEnabled = false

        return view
    }

    private fun sendContactDetails() {

        when {
            et_user_name?.text.toString().trim() == "" -> {
                et_user_name?.error = "Enter User name"
                Toast.makeText(context, "Enter User name", Toast.LENGTH_LONG).show()
            }
            et_unii_mail?.text.toString().trim() == "" -> {
                et_unii_mail?.error = "Enter Unii Mail"
                Toast.makeText(context, "Enter Unii Mail", Toast.LENGTH_LONG).show()
            }
            et_university?.text.toString().trim() == "" -> {
                et_university?.error = "Please update your course in profile"
                Toast.makeText(
                    context,
                    "Please update your university name in profile",
                    Toast.LENGTH_LONG
                ).show()
            }
            et_course?.text.toString().trim() == "" -> {
                et_course?.error = "Please update your course in profile"
                Toast.makeText(context, "Please update your course in profile", Toast.LENGTH_LONG)
                    .show()
            }
            et_query?.text.toString().trim() == "" -> {
                et_query?.error = "Enter Query"
                Toast.makeText(context, "Enter Query", Toast.LENGTH_LONG).show()
            }
            else -> {

                if (!checkEmail(et_unii_mail?.text.toString())) {
                    Toast.makeText(
                        context,
                        "Please enter correct email to Register",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    progressDialog?.setMessage("Submitting query..")
                    progressDialog?.setCancelable(false)
                    progressDialog?.show()
                    contactUsPresenter?.contactUs(
                        et_user_name?.text.toString().trim(),
                        et_unii_mail?.text.toString().trim(),
                        et_university?.text.toString().trim(),
                        et_course?.text.toString().trim(),
                        et_query?.text.toString().trim()
                    )
                }
            }
        }
    }

    private fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun initialVariables(view: View?) {
        btn_submit = view?.findViewById(R.id.btn_submit)
        et_user_name = view?.findViewById(R.id.et_user_name)
        et_unii_mail = view?.findViewById(R.id.et_unii_mail)
        et_university = view?.findViewById(R.id.et_university)
        et_course = view?.findViewById(R.id.et_course)
        et_query = view?.findViewById(R.id.et_query)
        progressDialog = ProgressDialog(context)
        contactUsPresenter = ContactUsPresenter(this, context)
    }

    override fun contactUsListenerResponse(status: String, message: String?) {
        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        if (status == "true") {
            Toast.makeText(context, "Your query has been sent successfully", Toast.LENGTH_LONG).show()
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                MoreFragment(), true, false
            )
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

    }
}
