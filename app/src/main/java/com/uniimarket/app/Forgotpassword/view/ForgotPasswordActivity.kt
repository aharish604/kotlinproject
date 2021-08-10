package com.uniimarket.app.Forgotpassword.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.uniimarket.app.Forgotpassword.presenter.ForgotPswdPresenter
import com.uniimarket.app.R
import com.uniimarket.app.Signin.view.SignInActivity

class ForgotPasswordActivity : AppCompatActivity(), ForgotPswdPresenter.ForgotPasswordListener {


    var et_pswd: TextInputEditText? = null
    var et_cnf_pswd: TextInputEditText? = null
    var et_veri_code: TextInputEditText? = null
    var btn_submit: Button? = null
    var email: String = ""
    var forgotPswdPresenter: ForgotPswdPresenter? = null
    var imv_cancel: ImageView? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_forgot_password)

        initialVariables()


        email = intent.getStringExtra("email")

        imv_cancel?.setOnClickListener {
            finish()
        }

        btn_submit?.setOnClickListener {

            when {
                et_pswd?.text.toString().trim().isEmpty() -> {
//                    et_pswd?.error = "Please enter Password"
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show()
                }
                et_pswd?.text.toString().length < 8
                        || et_pswd?.text.toString().length > 24 -> {
                    Toast.makeText(this, "Password length should be 8 to 24", Toast.LENGTH_LONG)
                        .show()
                }
                et_cnf_pswd?.text.toString().trim().isEmpty() -> {
//                    et_cnf_pswd?.error = "Please enter Confirm Password"
                    Toast.makeText(this, "Please enter Confirm Password", Toast.LENGTH_LONG).show()
                }

                et_cnf_pswd?.text.toString().length < 8
                        || et_cnf_pswd?.text.toString().length > 24 -> {
                    Toast.makeText(
                        this,
                        "Confirm Password length should be 8 to 24",
                        Toast.LENGTH_LONG
                    ).show()
                }

                et_pswd?.text.toString().trim() != et_cnf_pswd?.text.toString().trim() -> {
//                    et_cnf_pswd?.error =
//                        "Password and Confirm Password both should be same"
                    Toast.makeText(
                        this,
                        "Password and Confirm Password both should be same",
                        Toast.LENGTH_LONG
                    ).show()

                }

                et_veri_code?.text.toString().trim().isEmpty() -> {
                    Toast.makeText(
                        this,
                        "Please enter verification code, what you received in mail.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {

                    progressDialog?.setMessage("Loading...")
                    progressDialog?.setCancelable(false)
                    progressDialog?.show()

                    forgotPswdPresenter?.forgotPassword(
                        email,
                        et_pswd?.text.toString(),
                        et_cnf_pswd?.text.toString(),
                        et_veri_code?.text.toString()
                    )
                }
            }
        }
    }

    private fun initialVariables() {
        imv_cancel = findViewById(R.id.imv_cancel)
        et_pswd = findViewById(R.id.et_pswd)
        et_cnf_pswd = findViewById(R.id.et_cnf_pswd)
        et_veri_code = findViewById(R.id.et_veri_code)
        btn_submit = findViewById(R.id.btn_submit)

        progressDialog = ProgressDialog(this)

        forgotPswdPresenter = ForgotPswdPresenter(this, this)
    }

    override fun forgotPswdResponseSuccess(status: String, message: String?) {

        try {
            progressDialog?.dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
        if (status == "true") {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            startActivity(Intent(this@ForgotPasswordActivity, SignInActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }


    }
}
