package com.uniimarket.app.ChangePassword.view

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.uniimarket.app.ChangePassword.presenter.ChangePasswordPresenter
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.R
import com.uniimarket.app.more.MoreFragment

class ChangePasswordFragment : Fragment(), ChangePasswordPresenter.ChangePasswordListener {


    var et_pswd: TextInputEditText? = null
    var et_new_pswd: TextInputEditText? = null
    var et_cnf_new_pswd: TextInputEditText? = null
    var tv_unii_mail: TextView? = null
    var btn_submit: Button? = null

    var changePasswordPresenter: ChangePasswordPresenter? = null
    var sharedPref: SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater?.inflate(R.layout.change_password_fragment, container, false)

        initialVariables(view)

        (context as DashboardActivity).ll_toolbar?.visibility = View.VISIBLE

        sharedPref = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }

        tv_unii_mail?.text = sharedPref?.getString("email", null)

        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE

        (context as DashboardActivity).ll_back?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                R.id.frame_layout,
                MoreFragment(), true, false
            )
        }
        btn_submit?.setOnClickListener {
            when {
                et_pswd?.text.toString().trim().isEmpty() -> {
//                    et_pswd?.error = "Please enter Password"
                    Toast.makeText(context, "Please enter Password", Toast.LENGTH_LONG).show()
                }
                et_new_pswd?.text.toString().trim().isEmpty() -> {
//                    et_new_pswd?.error = "Please enter Confirm Password"
                    Toast.makeText(context, "Please enter Confirm Password", Toast.LENGTH_LONG).show()
                }

                et_new_pswd?.text.toString().trim().length < 8 || et_new_pswd?.text.toString().trim().length > 24 -> {
                    Toast.makeText(context, "New Password length should be 8 to 24", Toast.LENGTH_LONG).show()
                }

                et_cnf_new_pswd?.text.toString().trim().isEmpty() -> {
//                    et_cnf_new_pswd?.error =
//                        "Please enter Confirm Password"
                    Toast.makeText(context, "Please enter Confirm New Password", Toast.LENGTH_LONG).show()
                }
                et_cnf_new_pswd?.text.toString().trim().length < 8 || et_cnf_new_pswd?.text.toString().trim().length > 24 -> {
                    Toast.makeText(context, "Confirm New Password length should be 8 to 24", Toast.LENGTH_LONG).show()
                }



                et_new_pswd?.text.toString().trim() != et_cnf_new_pswd?.text.toString().trim() -> {
//                    et_new_pswd?.error =
//                        "Password and Confirm Password both should be same"

                    Toast.makeText(context, "Password and Confirm Password both should be same", Toast.LENGTH_LONG)
                        .show()
                }

                et_pswd?.text.toString().trim() != et_new_pswd?.text.toString().trim() -> {
                    Toast.makeText(context, "Password and new password should not be same", Toast.LENGTH_LONG).show()
                }

                else -> {
                    changePassword()
                }
            }
        }

        return view
    }

    private fun changePassword() {

        changePasswordPresenter?.changePassword(
            sharedPref?.getString("email", null),
            et_pswd?.text.toString(), et_new_pswd?.text.toString(), et_cnf_new_pswd?.text.toString()
        )
    }

    private fun initialVariables(view: View?) {
        et_pswd = view?.findViewById(R.id.et_pswd)
        et_new_pswd = view?.findViewById(R.id.et_new_pswd)
        et_cnf_new_pswd = view?.findViewById(R.id.et_cnf_new_pswd)
        tv_unii_mail = view?.findViewById(R.id.tv_unii_mail)
        btn_submit = view?.findViewById(R.id.btn_submit)

        changePasswordPresenter = ChangePasswordPresenter(this, context)
    }

    override fun changePswdResponse(status: String, message: String?) {

        if (status == "true") {
            openDialog(message)
        } else {
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
        }

    }


    private fun openDialog(message: String?) {

        val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.change_pswd_popup)
        val body = dialog.findViewById(R.id.imv_cancel) as ImageView
        val tv_security_text: TextView = dialog.findViewById(R.id.tv_security_text)
//        body.text = title
//        val yesBtn = dialog .findViewById(R.id.yesBtn) as Button
//        val noBtn = dialog .findViewById(R.id.noBtn) as TextView

        tv_security_text.text = message

        body.setOnClickListener {
            dialog.dismiss()

        }

        dialog.show()

    }
}
