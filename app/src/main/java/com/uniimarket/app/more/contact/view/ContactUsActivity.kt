package com.uniimarket.app.more.contact.view

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.uniimarket.app.R
import com.uniimarket.app.more.contact.presenter.ContactUsPresenter

class ContactUsActivity : AppCompatActivity(), ContactUsPresenter.ContactUsListener {

    var btn_submit: Button? = null
    var et_user_name: EditText? = null
    var et_unii_mail: EditText? = null
    var et_university: EditText? = null
    var et_course: EditText? = null
    var et_query: EditText? = null
    var contactUsPresenter: ContactUsPresenter? = null

    var ll_back: LinearLayout? = null
    var imv_profile: ImageView? = null
    var imv_cancel: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_us_fragment)


        initialVariables()
//        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE
//
        ll_back?.setOnClickListener {
            onBackPressed()
        }

        imv_cancel?.setOnClickListener {
            onBackPressed()
        }

        var sharedPref: SharedPreferences? = getSharedPreferences("uniimarket", 0)
        imv_profile?.let {
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
    }


    private fun sendContactDetails() {

        when {
            et_user_name?.text.toString().trim() == "" -> {
                et_user_name?.error = "Enter User name"
                Toast.makeText(this, "Enter User name", Toast.LENGTH_LONG).show()
            }
            et_unii_mail?.text.toString().trim() == "" -> {
                et_unii_mail?.error = "Enter Unii Mail"
                Toast.makeText(this, "Enter Unii Mail", Toast.LENGTH_LONG).show()
            }
            et_university?.text.toString().trim() == "" -> {
                et_university?.error = "Please update your course in profile"
                Toast.makeText(this, "Please update your university name in profile", Toast.LENGTH_LONG).show()
            }
            et_course?.text.toString().trim() == "" -> {
                et_course?.error = "Please update your course in profile"
                Toast.makeText(this, "Please update your course in profile", Toast.LENGTH_LONG).show()
            }
            et_query?.text.toString().trim() == "" -> {
                et_query?.error = "Enter Query"
                Toast.makeText(this, "Enter Query", Toast.LENGTH_LONG).show()
            }
            else -> {

                if (!checkEmail(et_unii_mail?.text.toString())) {
                    Toast.makeText(this, "Please enter correct email to Register", Toast.LENGTH_LONG).show()
                } else {
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

    private fun initialVariables() {
        ll_back = findViewById(R.id.ll_back)
        imv_profile = findViewById(R.id.imv_profile)
        imv_cancel = findViewById(R.id.imv_cancel)
        btn_submit = findViewById(R.id.btn_submit)
        et_user_name = findViewById(R.id.et_user_name)
        et_unii_mail = findViewById(R.id.et_unii_mail)
        et_university = findViewById(R.id.et_university)
        et_course = findViewById(R.id.et_course)
        et_query = findViewById(R.id.et_query)

        contactUsPresenter = ContactUsPresenter(this, this)
    }

    override fun contactUsListenerResponse(status: String, message: String?) {

        if (status == "true") {
            Toast.makeText(this, "Your Query Send Successfully", Toast.LENGTH_LONG).show()
//            (this as DashboardActivity).replaceFragment(
//                R.id.frame_layout,
//                MoreFragment(), true, false
//            )

            onBackPressed()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

    }
}
