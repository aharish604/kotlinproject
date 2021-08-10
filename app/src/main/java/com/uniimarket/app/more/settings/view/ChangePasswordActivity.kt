package com.uniimarket.app.more.settings.view

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.uniimarket.app.ChangePassword.presenter.ChangePasswordPresenter
import com.uniimarket.app.R

class ChangePasswordActivity : AppCompatActivity(), ChangePasswordPresenter.ChangePasswordListener {

    var et_pswd: TextInputEditText? = null
    var et_new_pswd: TextInputEditText? = null
    var et_cnf_new_pswd: TextInputEditText? = null
    var tv_unii_mail: TextView? = null
    var btn_submit: Button? = null
    var changePasswordPresenter: ChangePasswordPresenter? = null
    var sharedPref: SharedPreferences? = null
    var imv_cancel: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        initialVariables()

        imv_cancel?.setOnClickListener {
            finish()
        }

        sharedPref = getSharedPreferences("uniimarket", 0)
        tv_unii_mail?.text = sharedPref?.getString("email", null)

//        (context as DashboardActivity).ll_back?.visibility = View.VISIBLE
//
//        (context as DashboardActivity).ll_back?.setOnClickListener {
//            (context as DashboardActivity).replaceFragment(
//                R.id.frame_layout,
//                MoreFragment(), true, false
//            )
//        }

        btn_submit?.setOnClickListener {
            when {
                et_pswd?.text.toString().trim().isEmpty() -> {
//                    et_pswd?.error = "Please enter Password"
                    Toast.makeText(this, "Please enter current Password", Toast.LENGTH_LONG).show()
                }
                et_new_pswd?.text.toString().trim().isEmpty() -> {
//                    et_new_pswd?.error = "Please enter Confirm Password"
                    Toast.makeText(this, "Please enter New Password", Toast.LENGTH_LONG).show()
                }

                et_new_pswd?.text.toString().trim().length < 8 || et_new_pswd?.text.toString().trim().length > 24 -> {
                    Toast.makeText(this, "New Password length should be 8 to 24", Toast.LENGTH_LONG).show()
                }

                et_cnf_new_pswd?.text.toString().trim().isEmpty() -> {
//                    et_cnf_new_pswd?.error =
//                        "Please enter Confirm Password"
                    Toast.makeText(this, "Please enter Confirm New Password", Toast.LENGTH_LONG).show()
                }
                et_cnf_new_pswd?.text.toString().trim().length < 8 || et_cnf_new_pswd?.text.toString().trim().length > 24 -> {
                    Toast.makeText(this, "Confirm New Password length should be 8 to 24", Toast.LENGTH_LONG).show()
                }


                et_new_pswd?.text.toString().trim() != et_cnf_new_pswd?.text.toString().trim() -> {
//                    et_new_pswd?.error =
//                        "Password and Confirm Password both should be same"

                    Toast.makeText(this, "New Password and Confirm New Password both should be same", Toast.LENGTH_LONG)
                        .show()
                }

                else -> {
                    changePassword()
                }
            }
        }

    }


    private fun changePassword() {

        changePasswordPresenter?.changePassword(
            sharedPref?.getString("email", null),
            et_pswd?.text.toString(), et_new_pswd?.text.toString(), et_cnf_new_pswd?.text.toString()
        )


    }

    private fun initialVariables() {
        et_pswd = findViewById(R.id.et_pswd)
        et_new_pswd = findViewById(R.id.et_new_pswd)
        et_cnf_new_pswd = findViewById(R.id.et_cnf_new_pswd)
        tv_unii_mail = findViewById(R.id.tv_unii_mail)
        btn_submit = findViewById(R.id.btn_submit)
        imv_cancel = findViewById(R.id.imv_cancel)
        changePasswordPresenter = ChangePasswordPresenter(this, this)
    }


    override fun changePswdResponse(status: String, message: String?) {

        if (status == "true") {
            openDialog(message)
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

    }


    private fun openDialog(message: String?) {

        val dialog = Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar)
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
            finish()

        }

        dialog.show()

    }

}
